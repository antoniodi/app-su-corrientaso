package domain.model.types

sealed trait CardinalPointType {
  def description: String
}

case object North extends CardinalPointType {
  override def description: String = "Norte"
}
case object East extends CardinalPointType {
  override def description: String = "Oriente"
}
case object South extends CardinalPointType {
  override def description: String = "Sur"
}
case object West extends CardinalPointType {
  override def description: String = "Oeste"
}

