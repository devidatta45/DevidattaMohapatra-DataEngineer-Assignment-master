package ing.vyasya.runtime

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import ing.vyasya.domain.{FileError, FileOperation, LineResponse, ServerError, ValidationError}
import ing.vyasya.http.ApiCall
import zio.internal.{Platform, PlatformLive}
import ing.vyasya.domain.InputFileService._
import ing.vyasya.file.FileService

// This is the App which needs to run while testing the application live
// It expects 2 program arguments i.e input-file output-file ex: input.tx out.txt
object LiveRuntime extends zio.Runtime[LineResponse with FileOperation] with App with LazyLogging {
  override val environment: LineResponse with FileOperation = Live.env
  override val platform: Platform = PlatformLive.Default

  if(args.length < 2){
    throw new Exception("Not enough arguments passed")
  }

  val input = args.head
  val output = args.last
  val file = new File(input)

  val result = unsafeRun(service.convertToOutputFile(file, output).either)
  result match {
    case Right(response) => println(s"the response is $response")
    case Left(error) => error match {
      case ValidationError(message, _) => println(s"Validation failed with message: $message")
      case ServerError(message, code, exception) => println(s"Failed with message: $message code: $code and reason: ${exception.getMessage}")
      case FileError(message, code, exception) => println(s"Failed with message: $message code: $code and reason: ${exception.getMessage}")
    }
  }
}
// Live runtime to run the application
object Live {
  val env: LineResponse with FileOperation = LiveEnvironment

  object LiveEnvironment extends LineResponse with FileOperation{
    override val response: LineResponse.Service = ApiCall
    override val operation: FileOperation.Service = FileService
  }
}