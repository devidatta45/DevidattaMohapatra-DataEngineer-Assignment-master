package ing.vyasya.specs

import ing.vyasya.domain.GenericError
import ing.vyasya.model.LineSchema
import org.scalacheck.Gen

object EntityGenerator {
  def sample(id: String = s"job-${Gen.identifier.sample.get}",
             lines: Vector[String] = Vector("Random Line")): Either[GenericError, LineSchema] = {
    LineSchema.fromUnsafe(id, lines)
  }
}
