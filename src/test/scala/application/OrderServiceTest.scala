package application

import application.services.OderService
import org.mockito.Mockito.spy
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpyOrderService extends OderService

class OrderServiceTest extends AnyWordSpec with Matchers {

  "Calling doDeliveries" when {

    "the order is correct" should {
      "return the deliveries report" in {
        val spyOrderService = spy( new SpyOrderService )

      }
    }
  }


}
