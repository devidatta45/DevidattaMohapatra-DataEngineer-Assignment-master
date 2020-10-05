package ing.vyasya.specs
import ing.vyasya.domain.FileOperation
import ing.vyasya.inmemory.InMemoryFileResponseService

// Unit specs for in-memory file operation service
class InMemoryFileOperationSpec extends FileOperationSpec {
  override val operation: FileOperation.Service = InMemoryFileResponseService

  behavior of "InMemoryFileOperation"
}
