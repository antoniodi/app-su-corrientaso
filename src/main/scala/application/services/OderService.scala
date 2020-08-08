package application.services

import application.errors.{Done, ServiceError}
import domain.model.entities.{Delivery, Drone, Order}
import domain.services.ValidateDeliveryServices
import infrastructure.services.FileService

object OderService {

  def processOrders(): Either[ServiceError, Done] = {

//    ( FIRST_ORDER to DRONE_QUANTITY ).map {}

    val droneFileNumber = 1
    for {
      stringOrder <- newFileService.readFile( s"in${numberToFileNumber( droneFileNumber )}.txt")
      order <- stringOrderToOrder( stringOrder )
      _ <- newValidateDeliveryServices.validateDeliveries( order )
    } yield Done
  }

  private def numberToFileNumber( droneFileNumber: Int ): String = {
    val MAX_UNIT = 9
    if ( droneFileNumber <= MAX_UNIT ) s"0${droneFileNumber}" else droneFileNumber.toString
  }

  private def newFileService: FileService = {
    new FileService()
  }

  private def newValidateDeliveryServices: ValidateDeliveryServices = {
    new ValidateDeliveryServices()
  }

  private def stringOrderToOrder( stringOrder: List[String] ): Either[ServiceError, Order] = {
    Right( Order( stringOrder.map( Delivery ), Drone() ) )
  }

}
