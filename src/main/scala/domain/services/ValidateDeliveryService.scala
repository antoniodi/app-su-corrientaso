package domain.services

import application.errors.{Business, Done, ServiceError}
import domain.constants.DomainConstants.{FORWARD_STEP, INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y, MAX_DELIVERY_DISTANCE, MAX_LUNCHES_PER_TRAVEL}
import domain.model.entities.{Coordinate, Delivery, Drone, Order}

class ValidateDeliveryService {

  def validateDeliveries( order: Order ): Either[ServiceError, Done] = {
    for {
      _ <- validateMaxDeliveries( order.deliveries )
      _ <- validateMaxDistance( order.deliveries, order.drone )
    } yield Done
  }

  private[services] def validateMaxDeliveries( deliveries: List[Delivery] ): Either[ServiceError, Done] = {
    if ( deliveries.length > MAX_LUNCHES_PER_TRAVEL ) Left( ServiceError( Business, "The number of orders is too big" ) )
    else Right( Done )
  }

  private[services] def validateMaxDistance( deliveries: List[Delivery], drone: Drone ): Either[ServiceError, Done] = {
    val ONE_DELIVERY = 1
    deliveries.headOption match {
      case Some( delivery ) =>
        validateMaxDistance( delivery.instructions, drone ) match {
          case Right( droneInNewPosition ) => validateMaxDistance( deliveries.drop( ONE_DELIVERY ), droneInNewPosition )
          case Left( serviceError ) => Left( serviceError )
        }
      case None    => Right( Done )
    }
  }

  def validateMaxDistance( instructions: String, drone: Drone ): Either[ServiceError, Drone] = {
    val ONE_INSTRUCTION = 1
    instructions.headOption match {
      case Some( instruction ) =>
        drone.doInstruction( instruction ) match {
          case Right( droneInNewPosition ) =>
            if ( FORWARD_STEP == instruction ) {
              if ( getDistanceBetweenTwoPoints( Coordinate( INIT_DRONE_POSITION_X, INIT_DRONE_POSITION_Y), drone.position.coordinate ) > MAX_DELIVERY_DISTANCE )
                Left(  ServiceError( Business, s"Exceeded maximum distance of delivery ${MAX_DELIVERY_DISTANCE}. End position ${drone.position.coordinate.toString} ${drone.position.direction.description}" ) )
              else validateMaxDistance( instructions.drop( ONE_INSTRUCTION ), droneInNewPosition)
            } else validateMaxDistance( instructions.drop( ONE_INSTRUCTION ), droneInNewPosition)
          case error @ Left( _ ) => error
        }
      case None => Right( drone )
    }
  }

  def getDistanceBetweenTwoPoints(initCoordinate: Coordinate, endCoordinate: Coordinate ): Double = {
    val EXPONENT_TWO = 2
    Math.sqrt( Math.pow( Math.abs( endCoordinate.x - initCoordinate.x ), EXPONENT_TWO ) + Math.pow( Math.abs( endCoordinate.y - initCoordinate.y ), EXPONENT_TWO )  )
  }
}
