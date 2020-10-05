package ing.vyasya.domain

// Generic Error trait which will be used throughout the application to give business specific error message
sealed trait GenericError {
  val message: String
  val code: String
}

case class ApiError(message: String) extends Exception(message)

case class ServerError(message: String, code: String, exception: ApiError) extends GenericError

case class FileError(message: String, code: String = "FILE_ERROR", exception: ApiError) extends GenericError

case class ValidationError(message: String, code: String = "VALIDATION_FAILED") extends GenericError