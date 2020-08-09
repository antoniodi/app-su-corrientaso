package application.services

import application.constants.ApplicationConstants.{FILES_TO_READ_SOURCE, FILES_TO_WRITE_SOURCE, FIRST_ORDER}
import application.errors.{Done, ServiceError}
import domain.constants.DomainConstants.DRONE_QUANTITY
import domain.model.entities.{Delivery, Drone, Order}
import domain.services.{DeliveryService, ValidateDeliveryService}
import infrastructure.services.FileService

object OderService {

  def processOrders(): List[Either[ServiceError, Done]] = {
    ( FIRST_ORDER to DRONE_QUANTITY ).map(orderNumber => {
      processOrder(orderNumber)
    }).toList
  }

  private[services] def processOrder(orderNumber: Int): Either[ServiceError, Done] = {

    val fileInSource = s"${FILES_TO_READ_SOURCE}/in${numberToFileNumber(orderNumber)}.txt"
    val fileOutSource = s"${FILES_TO_WRITE_SOURCE}/out${numberToFileNumber(orderNumber)}.txt"
    for {
      stringOrder <- newFileService.readFile( fileInSource )
      order <- stringOrderToOrder(stringOrder)
      _ <- newValidateDeliveryServices.validateDeliveries(order)
      orderReport <- newDeliveryServices.doDeliveries(order)
      _ <- newFileService.writeFile( fileOutSource, orderReport.toReportString )
    } yield Done
  }

  private[services] def numberToFileNumber(droneFileNumber: Int ): String = {
    val MAX_UNIT = 9
    if ( droneFileNumber <= MAX_UNIT ) s"0${droneFileNumber}" else droneFileNumber.toString
  }

  private def newFileService: FileService = {
    new FileService()
  }

  private def newValidateDeliveryServices: ValidateDeliveryService = {
    new ValidateDeliveryService()
  }

  private def newDeliveryServices: DeliveryService = {
    new DeliveryService()
  }

  private def stringOrderToOrder( stringOrder: List[String] ): Either[ServiceError, Order] = {
    Right( Order( stringOrder.map( Delivery ), Drone() ) )
  }

}
