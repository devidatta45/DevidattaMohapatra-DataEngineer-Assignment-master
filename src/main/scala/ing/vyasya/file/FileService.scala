package ing.vyasya.file

import java.io.{File, FileOutputStream, PrintWriter}

import cats.data.NonEmptyList
import com.typesafe.scalalogging.LazyLogging
import ing.vyasya.config.ConfigResolver._
import ing.vyasya.domain.{ApiError, FileError, FileOperation, GenericError, ServerError, ValidationError}
import zio.{IO, Task, ZIO}

import scala.io.Source

// Live file implementation for the File operation service
object FileService extends LazyLogging with FileOperation.Service {
  override def readLines(file: File): IO[GenericError, Vector[Vector[String]]] = {
    val lines = Task.apply {
      val listOfLines = Source.fromFile(file).getLines.toList
      val nonEmptyList = NonEmptyList.fromList(listOfLines)
      val result = nonEmptyList.map { list =>
        list.toList.toVector.grouped(chunkSize).toVector
      }
      result.toRight(ValidationError("Number of lines can't be empty"))
    }
    normalise(lines)

  }

  private def normalise[T](task: Task[Either[GenericError, T]]): ZIO[Any, GenericError, T] = {
    for {
      either <- task.mapError(error => FileError(error.getMessage, exception = ApiError(error.getMessage)))
      result <- ZIO.fromEither(either)
    } yield result
  }

  override def writeToFile(list: Vector[String], outputFile: String): IO[GenericError, String] = {
    val task = Task.apply {
      val file = new File(outputFile)
      val bw = new PrintWriter(new FileOutputStream(file, true))
      val nonEmptyList = NonEmptyList.fromList(list.toList)
      val result = nonEmptyList.map { npList =>
        npList.map(line => bw.append(line + "\n"))
      }
      bw.close()
      result.toRight(ValidationError("Number of lines can't be zero")).map(_ => outputFile)
    }
    normalise(task)

  }
}
