package domain.model.entities

import application.errors.{Business, ServiceError}
import domain.constants.DomainConstants.{FORWARD_STEP, INIT_DRONE_DIRECTION, INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y, ROTATE_LEFT_NINETY_DEGREE, ROTATE_RIGHT_NINETY_DEGREE}
import domain.model.types.{CardinalPointType, East, North, South, West}
import com.softwaremill.quicklens._

final case class Drone( position: Position = Position( Coordinate( INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y ), INIT_DRONE_DIRECTION ) ) {

  private def moveForward(): Drone = {
    val ONE_STEP = 1
    this.position.direction match {
      case North => this.modify( _.position.coordinate.y ).setTo( ONE_STEP )
      case East  => this.modify( _.position.coordinate.x ).setTo( ONE_STEP )
      case South => this.modify( _.position.coordinate.y ).setTo( - ONE_STEP )
      case West  => this.modify( _.position.coordinate.x ).setTo( - ONE_STEP )
    }
  }

  private def rotateRight(): Drone = {
    this.modify( _.position.direction ).setTo( this.position.direction.rightCardinalPoint )
  }

  private def rotateLeft(): Drone = {
    this.modify( _.position.direction ).setTo( this.position.direction.leftCardinalPoint )
  }

  def doInstruction( instruction: Char ): Either[ServiceError, Drone] = {
    instruction match {
      case FORWARD_STEP               => Right( this.moveForward() )
      case ROTATE_LEFT_NINETY_DEGREE  => Right( this.rotateLeft() )
      case ROTATE_RIGHT_NINETY_DEGREE => Right( this.rotateRight() )
      case _ => Left(ServiceError(Business, "Invalid instruction"))
    }
  }
}
