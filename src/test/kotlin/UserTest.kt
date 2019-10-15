import models.Deck
import models.Field
import models.Hand
import models.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.reflect.full.*

internal class UserTest {

    @Test
    internal fun playerConstructorTest() {
        var player: Player = Player("TestNisse")
        var pigMonster: Card = Monster("Pig")
        var rabbitMonster: Card = Monster("Rabbit")
        var listOfCards: ArrayList<Card> = arrayListOf(pigMonster, rabbitMonster)
        var deck:Deck = Deck(false,listOfCards)
        var hand:Hand = Hand(false,listOfCards)
        var field:Field = Field(false,listOfCards)

        assertFalse(player::class.superclasses[0].primaryConstructor!!.parameters.find {it.name == "name"}!!.isOptional)
        assertTrue(
            player::class.superclasses[0].declaredMemberProperties.find { it.name == "id" }!!.toString().contains(
                "val"
            ), "Id should be immutable, declared as val"
        )
        assertTrue(
            player::class.declaredMemberProperties.find { it.name == "mana" }!!.toString().contains(
                "val"
            ), "Mana should be immutable, declared as val"
        )
        assertEquals("TestNisse", player.name, "Name does not match passed name")
        assertNotNull(player.id, "Player id is not allowed to be null")

        player = Player("TestGubbe",deck,hand,field)
        assertEquals("TestGubbe",player.name,"Name does not match passed name")
        assertEquals(deck,player.deck,"Deck does not match passed deck")
        assertEquals(hand,player.hand,"Hand does not match passed hand")
        assertEquals(field,player.field,"Field does not match passed field")

        try {
            player = Player("asdfghqwyew")
            assertTrue(false,"Name is too long, should throw illegalArgumentException")
        }catch(err:IllegalArgumentException){}

        try {
            player = Player("aas..1!#")
            assertTrue(false,"Name is too long, should throw illegalArgumentException")
        }catch(err:IllegalArgumentException){}
    }
}