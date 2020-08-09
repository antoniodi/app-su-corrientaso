package domain.model.entities

import application.errors.{Business, ServiceError}
import domain.model.types.{East, North, West}
import factories.DroneFactory._
import org.mockito.Matchers.any
import org.mockito.Mockito.{doReturn, never, spy, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DroneTest extends AnyWordSpec with Matchers {

  "drone" should {
    "be initialized correctly" in {
      drone.position.coordinate shouldEqual Coordinate( 0, 0)
      drone.position.direction shouldEqual North
    }
  }

  "Calling rotateRight" should {
    "return the drone with the cardinal point to right to the current cardinal point" in {
      drone.rotateRight().position.direction shouldEqual East
    }
  }

  "Calling rotateLeft" should {
    "return the drone with the cardinal point to left to the current cardinal point" in {
      drone.rotateLeft().position.direction shouldEqual West
    }
  }

  "Calling moveForward" when {

    "the current direction is North" should {
      "return the drone with one step to North" in {
        northDrone.moveForward().position.coordinate shouldEqual Coordinate( 0, 1)
      }
    }

    "the current direction is East" should {
      "return the drone with one step to East" in {
        eastDrone.moveForward().position.coordinate shouldEqual Coordinate( 1, 0)
      }
    }

    "the current direction is South" should {
      "return the drone with one step to South" in {
        southDrone.moveForward().position.coordinate shouldEqual Coordinate( 0, -1)
      }
    }

    "the current direction is West" should {
      "return the drone with one step to West" in {
        westDrone.moveForward().position.coordinate shouldEqual Coordinate( -1, 0)
      }
    }
  }

  "Calling doInstruction" when {

    "the instruction is A" should {
      "return the drone with one step to current direction" in {
        val spyDrone = spy( Drone() )
        doReturn( droneWithOneStepToNorth ).when( spyDrone ).moveForward()

        val result = spyDrone.doInstruction('A')

        result shouldEqual Right( droneWithOneStepToNorth )
        verify( spyDrone ).moveForward()
        verify( spyDrone, never ).rotateRight()
        verify( spyDrone, never ).rotateLeft()
      }
    }

    "the instruction is D" should {
      "return the drone with the cardinal point to right to the current cardinal point" in {
        val spyDrone = spy( Drone() )
        doReturn( eastDrone ).when( spyDrone ).rotateRight()

        val result = spyDrone.doInstruction('D')

        result shouldEqual Right( eastDrone )
        verify( spyDrone, never ).moveForward()
        verify( spyDrone ).rotateRight()
        verify( spyDrone, never ).rotateLeft()
      }
    }

    "the instruction is I" should {
      "return the drone with the cardinal point to left to the current cardinal point" in {
        val spyDrone = spy( Drone() )
        doReturn( westDrone ).when( spyDrone ).rotateLeft()

        val result = spyDrone.doInstruction('I')

        result shouldEqual Right( westDrone )
        verify( spyDrone, never ).moveForward()
        verify( spyDrone, never ).rotateRight()
        verify( spyDrone ).rotateLeft()
      }
    }

    "the instruction is different to A, I, D" should {
      "return a service error" in {
        val spyDrone = spy( Drone() )

        val result = spyDrone.doInstruction('K')

        result shouldEqual Left( ServiceError( Business, "Invalid instruction: K" ) )
        verify( spyDrone, never ).moveForward()
        verify( spyDrone, never ).rotateRight()
        verify( spyDrone, never ).rotateLeft()
      }
    }
  }

  "Calling doInstructions" when {

    "there are at least one instruction" should {
      "do first instruction and call again doInstructions with the rest of instructions" in {
        val spyDrone = spy( Drone() )
        val spyDroneWithInstruction = spy( Drone() )
        val instructions = "AI"
        val firstInstruction = 'A'
        val restOfInstructions = "I"
        doReturn( Right( spyDroneWithInstruction ) ).when( spyDrone ).doInstruction( firstInstruction )
        doReturn( Right( westDrone ) ).when( spyDroneWithInstruction ).doInstructions( restOfInstructions )

        val result = spyDrone.doInstructions( instructions )

        result shouldEqual Right( westDrone )
        verify( spyDrone ).doInstruction( firstInstruction )
        verify( spyDroneWithInstruction ).doInstructions( restOfInstructions )
      }
    }

    "there are one invalid instruction" should {
      "return error" in {
        val spyDrone = spy( Drone() )
        val instructions = "A"
        val firstInstruction = 'A'
        val expectedError = Left( ServiceError( Business, "Invalid instruction" ) )
        doReturn( expectedError ).when( spyDrone ).doInstruction( firstInstruction )

        val result = spyDrone.doInstructions( instructions )

        result shouldEqual expectedError
        verify( spyDrone ).doInstruction( firstInstruction )
        verify( spyDrone, never ).doInstructions( any[String] )
      }
    }

    "there are not instruction" should {
      "return current drone" in {
        val spyDrone = spy( Drone() )
        val instructions = ""

        val result = spyDrone.doInstructions( instructions )

        result shouldEqual Right( spyDrone )
        verify( spyDrone, never ).doInstruction( any[Char] )
        verify( spyDrone, never ).doInstructions( any[String] )
      }
    }
  }
}
