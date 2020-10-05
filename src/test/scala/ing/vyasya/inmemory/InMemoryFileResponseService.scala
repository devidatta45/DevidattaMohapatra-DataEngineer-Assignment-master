package ing.vyasya.inmemory

import java.io.File

import cats.data.NonEmptyList
import ing.vyasya.domain.{ApiError, FileError, FileOperation, GenericError, ValidationError}
import zio.{IO, ZIO}

// In-memory implementation for the File operation service
object InMemoryFileResponseService extends FileOperation.Service {
  override def readLines(file: File): IO[GenericError, Vector[Vector[String]]] = {
    val either = Either.cond(file.exists(), Vector(Vector("In memory lines"), Vector("In memory lines")), FileError("File does not exist", exception = ApiError("File does not exist")))
    ZIO.fromEither(either)
  }

  override def writeToFile(list: Vector[String], outputFile: String): IO[GenericError, String] = {
    val nonEmptyList = NonEmptyList.fromList(list.toList)
    val either = nonEmptyList.toRight(ValidationError("Number of lines can't be zero"))
    ZIO.fromEither(either).map(_ => outputFile)
  }
}

