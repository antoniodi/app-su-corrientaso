package factories

import domain.model.entities.{Coordinate, Position}
import domain.model.types.North

object PositionFactory {

  val position: Position = Position( Coordinate( 0, 0), North )

  val positions: List[Position] = List( Position( Coordinate( 0, 0), North ) )
}
