package domain.model.types

sealed trait CardinalPointType {
  def description: String
  def leftCardinalPoint: CardinalPointType
  def rightCardinalPoint: CardinalPointType
}

case object North extends CardinalPointType {
  override def description: String = "Norte"
  def leftCardinalPoint: CardinalPointType = West
  def rightCardinalPoint: CardinalPointType = East
}
case object East extends CardinalPointType {
  override def description: String = "Oriente"
  def leftCardinalPoint: CardinalPointType = North
  def rightCardinalPoint: CardinalPointType = South
}
case object South extends CardinalPointType {
  override def description: String = "Sur"
  def leftCardinalPoint: CardinalPointType = East
  def rightCardinalPoint: CardinalPointType = West
}
case object West extends CardinalPointType {
  override def description: String = "Oeste"
  def leftCardinalPoint: CardinalPointType = South
  def rightCardinalPoint: CardinalPointType = North
}

