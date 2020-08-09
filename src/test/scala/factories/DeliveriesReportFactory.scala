package factories

import domain.model.entities.DeliveriesReport
import factories.PositionFactory.position

object DeliveriesReportFactory {

  val deliveriesReport: DeliveriesReport = DeliveriesReport( List( position ) )

}
