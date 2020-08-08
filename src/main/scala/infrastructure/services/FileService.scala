package infrastructure.services

import java.io.{BufferedWriter, File, FileOutputStream, FileWriter, OutputStreamWriter}

import application.errors.{Done, ServiceError, Technical}

import scala.io.{BufferedSource, Source}

class FileService {

  def readFile( fileSource: String ): Either[ServiceError, List[String]] = {
    try {
      val sourceStream: BufferedSource = Source.fromFile( fileSource )
      val source = sourceStream.getLines.toList
      sourceStream.close()
      Right( source )
    } catch {
      case error : Throwable => Left( ServiceError( Technical, s"Cannot read file from route ${fileSource}" ) )
    }
  }

  def writeFile( fileSource: String, lines: List[String] ): Either[ServiceError, Done] = {
    try {
      val file = new File(fileSource)
      val bw = new BufferedWriter(new FileWriter(file))
      lines.foreach(bw.write)
      bw.close()
      Right( Done )
    } catch {
      case error : Throwable => Left(ServiceError(Technical, s"Cannot write file from route ${fileSource}"))
    }
  }

}
