package infrastructure.services

import application.errors.ServiceError

class FileService {

  def readFile( fileName: String ): Either[ServiceError, List[String]] = {
    Right( List( "AAAAIAA",
          "DDDAIAD",
          "AAIADAD" ) )
  }

}
