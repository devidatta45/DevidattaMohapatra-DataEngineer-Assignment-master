package ing.vyasya.specs

import ing.vyasya.domain.{LineResponse, ValidationError}
import org.scalatest.{EitherValues, Inside}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.DefaultRuntime
import ing.vyasya.specs.EntityGenerator._

// Abstract specs for line response service
abstract class LineResponseSpec extends AnyFlatSpec with Matchers with EitherValues with DefaultRuntime with Inside {

  val response: LineResponse.Service

  it should "get lines correctly for correctly created schema" in {
    val schema = sample()
    schema.isRight shouldBe true
    inside(schema){
      case Right(line) => unsafeRun(response.getLines(line)).lines.value.size shouldBe line.lines.value.size
    }
  }
  it should "get lines fail for schema created with wrong job id" in {
    sample(id = "some id") shouldBe Left(ValidationError("Invalid id: Predicate failed: \"some id\".startsWith(\"job\")."))
  }

  it should "fail for empty lines" in {
    sample(lines = Vector.empty) shouldBe Left(ValidationError("Can't have empty lines"))
  }
}
