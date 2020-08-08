package application.services

import application.errors.{Done, ServiceError}
import domain.model.entities.{Delivery, Drone, Order}
import domain.services.ValidateDeliveryServices
import infrastructure.services.FileService

class OderService {

  def processOrders(): Either[ServiceError, Done] = {

//    ( FIRST_ORDER to DRONE_QUANTITY ).map {}

    val droneFileNumber = 1
    for {
      stringOrder <- newFileService.readFile( s"in${numberToFileNumber( droneFileNumber )}.txt")
      order <- stringOrderToOrder( stringOrder )
      _ <- newValidateDeliveryServices.validateDeliveries( order )
    } yield Done
  }

  def numberToFileNumber( droneFileNumber: Int ): String = {
    val MAX_UNIT = 9
    if ( droneFileNumber <= MAX_UNIT ) s"0${droneFileNumber}" else droneFileNumber.toString
  }

  def newFileService: FileService = {
    new FileService()
  }

  def newValidateDeliveryServices: ValidateDeliveryServices = {
    new ValidateDeliveryServices()
  }

  def stringOrderToOrder( stringOrder: List[String] ): Either[ServiceError, Order] = {
    Right( Order( stringOrder.map( Delivery ), Drone() ) )
  }

}
