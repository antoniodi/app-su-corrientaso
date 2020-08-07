package domain.model.entities

import domain.constants.DomainConstants.{INIT_DRONE_DIRECTION, INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y}

case class Drone( position: Position = Position( Coordinate( INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y ), INIT_DRONE_DIRECTION ) )
