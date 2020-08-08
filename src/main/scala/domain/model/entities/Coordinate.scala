package domain.model.entities

case class Coordinate( x: Int, y: Int ) {
  override def toString: String = s"(${x}, ${y})"
}
