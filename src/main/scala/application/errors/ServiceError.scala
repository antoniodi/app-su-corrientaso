package application.errors

case class ServiceError( errorType: ErrorType, cause: String )
