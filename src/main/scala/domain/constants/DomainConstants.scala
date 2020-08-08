package domain.constants

import domain.model.types.{CardinalPointType, North}

object DomainConstants {

  val DRONE_QUANTITY = 20
  val MAX_DELIVERY_DISTANCE = 10
  val MAX_LUNCHES_PER_TRAVEL = 3

  val INIT_DRONE_POSITION_X = 0
  val INIT_DRONE_POSITION_Y = 0
  val INIT_DRONE_DIRECTION: CardinalPointType = North

  val FORWARD_STEP = 'A'
  val ROTATE_LEFT_NINETY_DEGREE = 'I'
  val ROTATE_RIGHT_NINETY_DEGREE = 'D'


}
