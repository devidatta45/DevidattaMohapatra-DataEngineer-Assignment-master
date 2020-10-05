package ing.vyasya.inmemory

import java.util.UUID

import ing.vyasya.domain.{GenericError, LineResponse}
import ing.vyasya.model.{LineSchema, LineSchemaRequest}
import zio.ZIO

// In-memory implementation for the line response service
object InMemoryLineResponseService extends LineResponse.Service {
  override def getLines(schema: LineSchema): ZIO[Any, GenericError, LineSchema] = {
    val request = LineSchemaRequest.from(schema)
    val changedLines = request.lines.map{line =>
      line + UUID.randomUUID()
    }
    val response = LineSchema.fromUnsafe(request.id, changedLines)
    ZIO.fromEither(response)
  }
}