package ing.vyasya.ispecs

import ing.vyasya.domain.FileOperation
import ing.vyasya.file.FileService
import ing.vyasya.specs.FileOperationSpec

// Integration specs for live file operation service
class FileOperationISpec extends FileOperationSpec {
  override val operation: FileOperation.Service = FileService

  behavior of "FileOperation"
}
