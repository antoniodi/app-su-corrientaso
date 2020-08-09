package domain.services

import application.errors.{Business, Done, ServiceError}
import domain.model.entities.{Delivery, Drone}
import factories.DeliveryFactory.{fourDeliveries, getDeliveries}
import factories.DroneFactory.drone
import factories.OrderFactory
import org.mockito.Matchers.any
import org.mockito.Mockito.{doReturn, never, spy, verify}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpyValidateDeliveryService extends ValidateDeliveryService

class ValidateDeliveryServiceTest extends AnyWordSpec with Matchers {

  "Calling validateDeliveries" when {

    "the order is correct" should {
      "return Done" in {
        val spyValidateDeliveryService = spy( new SpyValidateDeliveryService )
        val order = OrderFactory.order
        val deliveries = getDeliveries
        doReturn( Right( Done ) ).when( spyValidateDeliveryService ).validateMaxDeliveries( deliveries )
        doReturn( Right( Done ) ).when( spyValidateDeliveryService ).validateMaxDistance( deliveries, drone )

        val result = spyValidateDeliveryService.validateDeliveries( order )

        result shouldBe Right( Done )
        verify( spyValidateDeliveryService ).validateMaxDeliveries( deliveries )
        verify( spyValidateDeliveryService ).validateMaxDistance( deliveries, drone )
      }
    }

    "occur an error" should {
      "return the Error" in {
        val spyValidateDeliveryService = spy( new SpyValidateDeliveryService )
        val order = OrderFactory.order
        val deliveries = getDeliveries
        val error = Left( ServiceError( Business, "An error occurred" ) )
        doReturn( error ).when( spyValidateDeliveryService ).validateMaxDeliveries( deliveries )

        val result = spyValidateDeliveryService.validateDeliveries( order )

        result shouldBe error
        verify( spyValidateDeliveryService ).validateMaxDeliveries( any[List[Delivery]] )
        verify( spyValidateDeliveryService, never ).validateMaxDistance( any[List[Delivery]], any[Drone] )
      }
    }
  }

  "Calling validateDeliveries" when {

    "the order not exceeds the maximum allowed" should {
      "return Done" in {
        val deliveries = getDeliveries

        val result = new SpyValidateDeliveryService().validateMaxDeliveries( deliveries )

        result shouldBe Right( Done )
      }
    }

    "the order exceeds the maximum allowed" should {
      "return error" in {
        val deliveries = fourDeliveries
        val error = Left( ServiceError( Business, s"The number of deliveries exceeds the maximum allowed 3" ) )

        val result = new SpyValidateDeliveryService().validateMaxDeliveries( deliveries )

        result shouldBe error
      }
    }
  }

}
