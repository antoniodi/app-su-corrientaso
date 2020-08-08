package domain.model.entities

import domain.constants.DomainConstants.REPORT_TITLE

case class DeliveriesReport( finalDronePositions: List[Position]) {

  def toReportString: List[String] = {
    List( s"${REPORT_TITLE}\n\n" ) ++
      finalDronePositions.map( finalDronePosition =>
        s"${finalDronePosition.coordinate.toString} dirección ${finalDronePosition.direction.description}\n\n"
      )
  }
}
