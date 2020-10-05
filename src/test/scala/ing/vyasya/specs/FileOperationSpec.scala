package ing.vyasya.specs

import java.io.File

import ing.vyasya.domain.FileOperation
import org.scalatest.{EitherValues, Inside}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.DefaultRuntime

// Abstract specs for File operation service
abstract class FileOperationSpec extends AnyFlatSpec with Matchers with EitherValues with DefaultRuntime with Inside {

  val operation: FileOperation.Service

  it should "return lines in proper format with correct file" in {
    unsafeRun(operation.readLines(new File("src/test/resources/sample.txt"))).size shouldBe 2
  }

  it should "fail for non-existent file" in {
    val either = unsafeRun(operation.readLines(new File("src/test/resources/some.txt")).either)
    inside(either){
      case Left(error) => error.code shouldBe "FILE_ERROR"
    }
  }

  it should "write lines correctly" in {
    unsafeRun(operation.writeToFile(Vector("some text"), "out.txt")) shouldBe "out.txt"
  }

  it should "fail if writing empty lines" in {
    val either = unsafeRun(operation.writeToFile(Vector.empty, "out.txt").either)
    inside(either){
      case Left(error) => error.message shouldBe "Number of lines can't be zero"
    }
  }
}
