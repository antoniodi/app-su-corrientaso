package factories

import domain.model.entities.Order
import factories.DeliveryFactory.getDeliveries
import factories.DroneFactory.drone

object OrderFactory {

  val order: Order = Order( getDeliveries, drone )

}
