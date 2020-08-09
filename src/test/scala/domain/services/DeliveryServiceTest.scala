package domain.services

import application.errors.{Business, ServiceError}
import domain.model.entities.{Delivery, Drone, Position}
import factories.DeliveriesReportFactory.deliveriesReport
import factories.DeliveryFactory.getDeliveries
import factories.DroneFactory.drone
import factories.OrderFactory.order
import factories.PositionFactory.{position, positions, testCasePosition}
import org.mockito.Matchers.any
import org.mockito.Mockito.{doReturn, never, spy, verify}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpyDeliveryService extends DeliveryService

class DeliveryServiceTest extends AnyWordSpec with Matchers {

  "Calling doDeliveries" when {

    "the order is correct" should {
      "return the deliveries report" in {
        val spyDeliveryServices = spy( new SpyDeliveryService() )
        doReturn( Right( List( position ) ) ).when( spyDeliveryServices ).doDeliveries( getDeliveries, drone, Nil )

        val result = spyDeliveryServices.doDeliveries( order )

        result shouldBe Right( deliveriesReport )
        verify( spyDeliveryServices ).doDeliveries( getDeliveries, drone, Nil )
      }
    }

    "occur an error" should {
      "return the Error" in {
        val spyDeliveryServices = spy( new SpyDeliveryService() )
        doReturn( Left( ServiceError( Business, "An error occurred" ) ) ).when( spyDeliveryServices ).doDeliveries( getDeliveries, drone, Nil )

        val result = spyDeliveryServices.doDeliveries( order )

        result shouldBe Left( ServiceError( Business, "An error occurred" ) )
        verify( spyDeliveryServices ).doDeliveries( getDeliveries, drone, Nil )
      }
    }
  }

  "Calling doDeliveries" when {

    "there are at least one instruction" should {
      "do first instruction and call again doInstructions with the rest of instructions" in {
        val spyDeliveryServices = spy( new SpyDeliveryService() )
        val spyDrone = spy( Drone() )
        val spyDroneWithInstruction = spy( Drone() )
        val instructions = "AAI"
        val deliveries = getDeliveries( instructions )
        val restDeliveries = Nil
        val expectedPositions = Right( List( position ) )
        doReturn( Right( spyDroneWithInstruction ) ).when( spyDrone ).doInstructions( instructions )
        doReturn( expectedPositions ).when( spyDeliveryServices ).doDeliveries( restDeliveries, spyDroneWithInstruction, List( position ) )

        val result = spyDeliveryServices.doDeliveries( deliveries, spyDrone, Nil )

        result shouldEqual expectedPositions
        verify( spyDrone ).doInstructions( instructions )
        verify( spyDeliveryServices ).doDeliveries( restDeliveries, spyDroneWithInstruction, List( position )  )
      }
    }

    "doInstructions fail" should {
      "return error" in {
        val spyDeliveryServices = spy( new SpyDeliveryService() )
        val spyDrone = spy( Drone() )
        val instructions = "AAI"
        val deliveries = getDeliveries( instructions )
        val expectedPositions = Left( ServiceError( Business, "An error occurred" ) )
        doReturn( Left( ServiceError( Business, "An error occurred" ) ) ).when( spyDrone ).doInstructions( instructions )

        val result = spyDeliveryServices.doDeliveries( deliveries, spyDrone, Nil )

        result shouldEqual expectedPositions
        verify( spyDrone ).doInstructions( instructions )
      }
    }

    "there are not deliveries" should {
      "return current list of positions" in {
        val spyDeliveryServices = spy( new SpyDeliveryService() )
        val spyDrone = spy( Drone() )
        val deliveries = Nil
        val expectedPositions = positions

        val result = spyDeliveryServices.doDeliveries( deliveries, spyDrone, expectedPositions )

        result shouldEqual Right( expectedPositions )
        verify( spyDrone, never ).doInstructions( any[String] )
      }
    }

    "example order case are delivery (integration test)"  should {
      "return correct list of positions" in {
        val exampleDeliveries = getDeliveries
        val defaultDrone = Drone()
        val expectedPositions = testCasePosition

        val result =  new SpyDeliveryService().doDeliveries( exampleDeliveries, defaultDrone )

        result shouldEqual Right( expectedPositions )
      }
    }
  }


}
