package domain.model.entities

import domain.constants.DomainConstants.REPORT_TITLE

case class DeliveriesReport( finalDronePositions: List[Position]) {

  def toReportString: List[String] = {
    List( REPORT_TITLE ) ++
      finalDronePositions.map( finalDronePosition =>
        s"(${finalDronePosition.coordinate.x},${finalDronePosition.coordinate.y} dirección ${finalDronePosition.direction.toString}"
      )
  }
}
