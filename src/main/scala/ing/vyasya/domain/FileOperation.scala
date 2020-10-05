package ing.vyasya.domain

import java.io.File

import zio.IO

// Domain trait which holds the skeleton for the operations which will happen with the file
trait FileOperation {
  val operation: FileOperation.Service
}

object FileOperation {
  // As file operation is a impure task so using IO monad
  trait Service {
    def readLines(file: File): IO[GenericError, Vector[Vector[String]]]
    def writeToFile(list: Vector[String], outputFile: String): IO[GenericError, String]
  }
}