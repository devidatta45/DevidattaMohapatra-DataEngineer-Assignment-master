package ing.vyasya.http

import com.typesafe.scalalogging.LazyLogging
import ing.vyasya.domain.{ApiError, GenericError, LineResponse, ServerError}
import ing.vyasya.model.{LineSchema, LineSchemaRequest}
import zio.{IO, Task, ZIO}
import com.softwaremill.sttp._
import ing.vyasya.config.ConfigResolver._
import java.time.LocalDateTime

import ing.vyasya.utils.JsonUtility
import ing.vyasya.utils.DateUtility._

import scala.concurrent.ExecutionContext.Implicits.global

// Live file implementation for the File line response service
object ApiCall extends LazyLogging with LineResponse.Service with JsonUtility {

  def sttp: RequestT[Empty, String, Nothing] = com.softwaremill.sttp.sttp
  val applicationJson = "application/json"

  override def getLines(schema: LineSchema): ZIO[Any, GenericError, LineSchema] = {

    logger.info("Starting api call to get response")
    val request = LineSchemaRequest.from(schema)

    for{
      response <- callEndpoint(request)
      result   <- ZIO.fromEither(LineSchema.fromUnsafe(response.id, response.lines))
    } yield result

  }

  private def callEndpoint(request: LineSchemaRequest): IO[GenericError, LineSchemaRequest] = {
    val startTime = LocalDateTime.now().toEpochMilli

    val url = uri"$hostUrl/api/service"

    val data = marshal(request)
    val result = sttp
      .post(url)
      .contentType(applicationJson)
      .body(data)
      .response(asString)
      .send()
      .map(resolveResponse) map { res =>
      val totalTime = LocalDateTime.now().toEpochMilli - startTime
      logger.info(s"Time taken by the api call: $totalTime")
      res
    }
    val res = ZIO.fromFuture { implicit ctx => result }
    normalise(res)
  }

  private def normalise(task: Task[Either[GenericError, LineSchemaRequest]]): IO[GenericError, LineSchemaRequest] = {
    for{
      either <- task.mapError(error => ServerError(error.getMessage,error.getMessage, ApiError(error.getMessage)))
      result <- ZIO.fromEither(either)
    } yield result
  }

  private def resolveResponse(res: Response[String]): Either[GenericError, LineSchemaRequest] = res.body match {
    case Right(response) if res.isSuccess => Right(unmarshal[LineSchemaRequest](response))
    case Right(_) => Left(ServerError(res.statusText, res.code.toString, ApiError("Service call failed")))
    case Left(s) => Left(ServerError(res.statusText, res.code.toString, ApiError(s)))
  }
}
