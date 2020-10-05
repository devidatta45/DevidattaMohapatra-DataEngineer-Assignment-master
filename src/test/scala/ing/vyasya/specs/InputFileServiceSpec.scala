package ing.vyasya.specs

import java.io.File

import ing.vyasya.domain.{ApiError, FileError, InputFileService, ValidationError}
import ing.vyasya.inmemory.InMemoryRuntime
import ing.vyasya.specs.EntityGenerator._
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// Unit specs for in-memory input file service
class InputFileServiceSpec extends AnyFlatSpec with Matchers with EitherValues with InMemoryRuntime {

  private lazy val inputFileService: InputFileService = InputFileService.service

  behavior of "InputFileService"

  it should "create proper line schema with correct values" in {
    sample().isRight shouldBe true
  }

  it should "fail for improper job id" in {
    sample(id = "some id") shouldBe Left(ValidationError("Invalid id: Predicate failed: \"some id\".startsWith(\"job\")."))
  }

  it should "fail for empty lines" in {
    sample(lines = Vector.empty) shouldBe Left(ValidationError("Can't have empty lines"))
  }

  it should "run service properly and return correct result" in {
    unsafeRun(inputFileService.convertToOutputFile(new File("src/test/resources/dummy.txt"), "out.txt")) shouldBe "out.txt"
  }

  it should "fail for not existing file" in {
    unsafeRun(inputFileService.convertToOutputFile(new File("some-other.txt"), "out.txt").either) shouldBe
      Left(FileError("File does not exist", "FILE_ERROR", ApiError("File does not exist")))
  }

}
