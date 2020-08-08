package domain.model.entities

import domain.constants.DomainConstants.REPORT_TITLE

case class OrderReport( deliveriesReport: List[DeliveryReport]) {

  def toReportString: List[String] = {
    List( REPORT_TITLE ) ++ deliveriesReport.map( _.toString )
  }
}
