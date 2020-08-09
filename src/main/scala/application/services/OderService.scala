package application.services

import application.constants.ApplicationConstants.{FILES_TO_READ_SOURCE, FILES_TO_WRITE_SOURCE, FIRST_ORDER}
import application.errors.{Done, ServiceError}
import domain.constants.DomainConstants.DRONE_QUANTITY
import domain.model.entities.{Delivery, Drone, Order}
import domain.services.{DeliveryService, ValidateDeliveryService}
import infrastructure.services.FileService

trait OderService {

  def processOrders(): List[Either[ServiceError, Done]] = {
    ( FIRST_ORDER to DRONE_QUANTITY ).map(orderNumber => {
      processOrder(orderNumber)
    }).toList
  }

  private[services] def processOrder(orderNumber: Int): Either[ServiceError, Done] = {

    val fileInSource = s"${FILES_TO_READ_SOURCE}/in${numberToFileNumber(orderNumber)}.txt"
    val fileOutSource = s"${FILES_TO_WRITE_SOURCE}/out${numberToFileNumber(orderNumber)}.txt"
    for {
      stringOrder <- getFileService.readFile( fileInSource )
      order <- stringOrderToOrder(stringOrder)
      _ <- getValidateDeliveryServices.validateDeliveries(order)
      orderReport <- getDeliveryServices.doDeliveries(order)
      _ <- getFileService.writeFile( fileOutSource, orderReport.toReportString )
    } yield Done
  }

  private[services] def numberToFileNumber(droneFileNumber: Int ): String = {
    val MAX_UNIT = 9
    if ( droneFileNumber <= MAX_UNIT ) s"0${droneFileNumber}" else droneFileNumber.toString
  }

  private[services] def getFileService: FileService = {
    FileService
  }

  private[services] def getValidateDeliveryServices: ValidateDeliveryService = {
    ValidateDeliveryService
  }

  private[services] def getDeliveryServices: DeliveryService = {
    DeliveryService
  }

  private[services] def stringOrderToOrder( stringOrder: List[String] ): Either[ServiceError, Order] = {
    Right( Order( stringOrder.map( Delivery ), Drone() ) )
  }

}

object OderService extends OderService
