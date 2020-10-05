package ing.vyasya.domain

import java.io.{File, FileOutputStream, PrintWriter}
import java.util.UUID
import ing.vyasya.config.ConfigResolver._

import ing.vyasya.model.{LineSchema, LineSchemaRequest}
import zio.ZIO

import scala.io.Source

// Service responsible for converting input file lines to output lines and write to the output file
trait InputFileService {
  def convertToOutputFile(file: File, outputFile: String): ZIO[LineResponse with FileOperation, GenericError, String]
}

object InputFileService {

  val service: InputFileService = (file: File, outputFile: String) => {
    val uuid = UUID.randomUUID()
    for {
      allLineChunks <- ZIO.accessM[FileOperation](_.operation.readLines(file))
      file          <- ZIO.traverse(allLineChunks) { chunk =>
                         val request = LineSchemaRequest(s"job-$uuid", chunk)
                         val lineSchema = LineSchema.fromUnsafe(request.id, request.lines)
                         for {
                             schema      <- ZIO.fromEither(lineSchema)
                             result      <- ZIO.accessM[LineResponse](_.response.getLines(schema))
                             writeResult <- ZIO.accessM[FileOperation](_.operation.writeToFile(result.lines.value, outputFile))
                         } yield writeResult
                       }.map(_.headOption.getOrElse(outputFile))
    } yield file
  }

}
