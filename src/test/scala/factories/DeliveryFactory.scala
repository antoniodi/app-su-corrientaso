package factories

import domain.model.entities.Delivery

object DeliveryFactory {

  val delivery: Delivery = Delivery( "AAAAIAA" )

  def getDeliveries = List( Delivery( "AAAAIAA" ),
                            Delivery( "DDDAIAD" ),
                            Delivery( "AAIADAD" ) )

  def getDelivery( instruction: String ) = Delivery( instruction )

  def getDeliveries( instruction: String ) = List( Delivery( instruction ) )



}
