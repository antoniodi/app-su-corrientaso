package domain.services

import application.errors.ServiceError
import domain.model.entities.{Delivery, Drone, Order, DeliveriesReport, Position}

trait DeliveryService {

  def doDeliveries(order: Order ): Either[ServiceError, DeliveriesReport] = {

    for {
      deliveriesPositions <- doDeliveries( order.deliveries, order.drone )
    } yield DeliveriesReport( deliveriesPositions )
  }

  private[services] def doDeliveries( deliveries: List[Delivery], drone: Drone, deliveriesPositions: List[Position] = Nil ): Either[ServiceError, List[Position]] = {
    val ONE_DELIVERY = 1
    deliveries.headOption match {
      case Some( delivery ) =>
        drone.doInstructions( delivery.instructions ) match {
          case Right( droneInNewPosition ) => doDeliveries( deliveries.drop( ONE_DELIVERY ), droneInNewPosition, deliveriesPositions :+ droneInNewPosition.position )
          case Left( error ) => Left( error )
        }
      case None => Right( deliveriesPositions )
    }
  }
}

object DeliveryService extends DeliveryService

