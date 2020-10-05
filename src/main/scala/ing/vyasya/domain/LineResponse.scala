package ing.vyasya.domain

import ing.vyasya.model.LineSchema
import zio.IO

// Domain trait which holds the skeleton for the operations which will happen while calling the 3rd party api
trait LineResponse {
  val response: LineResponse.Service
}

object LineResponse {
  // As file operation is a impure task so using IO monad
  trait Service {
    def getLines(schema: LineSchema): IO[GenericError, LineSchema]
  }
}