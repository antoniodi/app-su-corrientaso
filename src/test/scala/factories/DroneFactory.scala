package factories

import domain.model.entities.{Coordinate, Drone}
import com.softwaremill.quicklens._
import domain.model.types.{East, North, South, West}

object DroneFactory {

  val drone: Drone = Drone()

  val northDrone: Drone = Drone().modify( _.position.direction ).setTo( North )
  val eastDrone: Drone = Drone().modify( _.position.direction ).setTo( East )
  val southDrone: Drone = Drone().modify( _.position.direction ).setTo( South )
  val westDrone: Drone = Drone().modify( _.position.direction ).setTo( West )

  val droneWithOneStepToNorth: Drone = Drone().modify( _.position.coordinate ).setTo( Coordinate( 0, 1) )

}
