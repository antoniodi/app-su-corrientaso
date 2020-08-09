package application.services

import application.errors.{Application, Done, ServiceError}
import domain.model.entities.Order
import domain.services.{DeliveryService, ValidateDeliveryService}
import factories.DeliveriesReportFactory.deliveriesReport
import infrastructure.services.FileService
import org.mockito.Matchers.{any, anyString}
import org.mockito.Mockito.{doReturn, spy, times, verify}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SpyOrderService extends OderService
class SpyFileService extends FileService
class SpyValidateDeliveryService extends ValidateDeliveryService
class SpyDeliveryService extends DeliveryService

class OrderServiceTest extends AnyWordSpec with Matchers {

  "Calling processOrders" should {
    "return with the result of deliveries" in {
      val spyOrderService = spy(new SpyOrderService)
      doReturn(Right(Done)).when(spyOrderService).processOrder(any[Int])
      val error = Left(ServiceError(Application, "An error occurred"))
      doReturn(error).when(spyOrderService).processOrder(3)
      val expectedResult = List(Right(Done), Right(Done), error, Right(Done), Right(Done),
        Right(Done), Right(Done), Right(Done), Right(Done), Right(Done),
        Right(Done), Right(Done), Right(Done), Right(Done), Right(Done),
        Right(Done), Right(Done), Right(Done), Right(Done), Right(Done))

      val result = spyOrderService.processOrders()

      result shouldEqual expectedResult
      verify(spyOrderService, times(20)).processOrder(any[Int])
    }
  }

  "Calling processOrder" when {

    "the order was red correctly" should {
      "return Done" in {
        val spyOrderService = spy( new SpyOrderService )
        val spyFileService = spy( new SpyFileService )
        val spyValidateDeliveryService = spy( new SpyValidateDeliveryService )
        val spyDeliveryService = spy( new SpyDeliveryService )
        val fileNumber = 1
        val file = List( "file1" )
        doReturn( spyFileService ).when( spyOrderService ).getFileService
        doReturn( Right( file ) ).when( spyFileService ).readFile( any[String] )
        doReturn( Right( Done ) ).when( spyFileService ).writeFile( any[String], any[List[String]] )
        doReturn( spyValidateDeliveryService ).when( spyOrderService ).getValidateDeliveryServices
        doReturn( Right( Done ) ).when( spyValidateDeliveryService ).validateDeliveries( any[Order] )
        doReturn( spyDeliveryService ).when( spyOrderService ).getDeliveryServices
        doReturn( Right( deliveriesReport ) ).when( spyDeliveryService ).doDeliveries( any[Order] )

        val result = spyOrderService.processOrder( fileNumber )

        result shouldEqual Right( Done )
      }
    }
  }


}
