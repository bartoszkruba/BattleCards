import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameTest {

    @Test
    fun nextTurn() {
        var game:Game = Game()
        game.nextTurn()
        assertEquals(1,game.turn,"Turn should have increased by one")
    }
}