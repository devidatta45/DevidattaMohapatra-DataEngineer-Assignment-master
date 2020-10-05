package ing.vyasya.model

import eu.timepit.refined
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string.StartsWith
import eu.timepit.refined.W
import ing.vyasya.model.LineSchema.LineSchemaId
import cats.syntax.either._
import ing.vyasya.domain.{GenericError, ValidationError}

// Necessary request/view model for accepting or interacting with others
case class LineSchemaRequest(
                              id: String,
                              lines: Vector[String]
                            )
object LineSchemaRequest{
  def from(lineSchema: LineSchema): LineSchemaRequest = LineSchemaRequest(lineSchema.id.value, lineSchema.lines.value)
}

// Necessary domain model which holds domain specific validations
case class LineSchema(
                       id: String Refined LineSchemaId,
                       lines: Vector[String] Refined NonEmpty
                     )

object LineSchema {
  type LineSchemaId = StartsWith[W.`"job"`.T]

  def fromUnsafe(id: String, lines: Vector[String]): Either[GenericError, LineSchema] = {
    for {
      id    <- fromId(id)
      lines <- fromLines(lines)
    } yield LineSchema(id, lines)
  }

  def fromId(id: String): Either[ValidationError, Refined[String, LineSchemaId]] = {
    refined.refineV[LineSchemaId](id).leftMap(error => ValidationError(s"Invalid id: $error"))
  }

  def fromLines(lines: Vector[String]): Either[ValidationError, Refined[Vector[String], NonEmpty]] = {
    refined.refineV[NonEmpty](lines).leftMap(_ => ValidationError("Can't have empty lines"))
  }
}
