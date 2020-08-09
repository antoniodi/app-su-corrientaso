package factories

import domain.model.entities.{Coordinate, Position}
import domain.model.types.{North, South, West}

object PositionFactory {

  val position: Position = Position( Coordinate( 0, 0), North )

  val positions: List[Position] = List( Position( Coordinate( 0, 0), North ) )

  val testCasePosition: List[Position] = List( Position( Coordinate( -2, 4 ), West ),
                                               Position( Coordinate( -1, 3 ), South ),
                                               Position( Coordinate( 0, 0 ), West ) )
}
