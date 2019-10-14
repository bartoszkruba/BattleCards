import Models.Deck
import Models.Field
import Models.Hand
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*

internal class CardListTest {

    @Test
    internal fun addCardTest() {
        var ogreCard: Monster = Monster("Ogre")
        var wolfCard: Monster = Monster("Wolf")
        var deck: Deck = Deck()

        assertTrue(deck.addCard(ogreCard))
        assertEquals(1, deck.cards.size, "The card wasn't added to the list")
        assertEquals(deck.cards[0], ogreCard, "Added card doesn't match the card that was added")

        ogreCard.attack = 20
        assertEquals(deck.cards[0], ogreCard, "Added card is not a copy of original object")

        assertTrue(deck.addCard(wolfCard))
        assertEquals(2, deck.cards.size, "The card wasn't added to the list")
        assertEquals(deck.cards[1], wolfCard, "Added card doesn't match the card that was added")

        wolfCard.attack = 29
        assertEquals(deck.cards[1], wolfCard, "Added card is not a copy of original object")
    }

    @Test
    internal fun removeCardTest() {
        var pigMonster: Card = Monster("Pig")
        var rabbitMonster: Card = Monster("Rabbit")
        var deck: Deck = Deck(false, arrayListOf(pigMonster))

        var removedCard: Card = Monster()

        try {
            removedCard = deck.removeCard(pigMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(pigMonster, removedCard, "The removed card doesn't match the card we wanted to remove")
        assertEquals(0, deck.cards.size, "The card did not get removed, a card still exists in the deck")
        assertEquals(true, deck.empty, "The boolean empty should be set to true because no cards exists")

        deck = Deck(false, arrayListOf(rabbitMonster, pigMonster))

        try {
            removedCard = deck.removeCard(rabbitMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(rabbitMonster, removedCard, "The removed card doesn't match the card we wanted to remove")
        assertEquals(1, deck.cards.size, "The card did not get removed, card array size is not correct")
        assertEquals(pigMonster, deck.cards[0], "The card that should exist is not the on existing")

        try {
            deck.removeCard(rabbitMonster)
            assertTrue(false, "Trying to remove a card that does not exist,should throw runtimeException")
        } catch (error: RuntimeException) {
        }
    }

    @Test
    internal fun handConstructorTest() {
        cardListConstructorTest(Hand::class)
        var hand:Hand = Hand()
        assertEquals(30,hand.maxSize)
    }

    @Test
    internal fun fieldConstructorTest() {
        cardListConstructorTest(Field::class)
        var field:Field = Field()
        assertEquals(30,field.maxSize)
    }

    @Test
    internal fun deckConstructorTest() {
        cardListConstructorTest(Deck::class)
        var deck:Deck = Deck()
        assertEquals(30,deck.maxSize)
    }

    private fun cardListConstructorTest(kClass: KClass<*>) {
        var pigMonster: Card = Monster("Pig")
        var rabbitMonster: Card = Monster("Rabbit")
        var listOfCards: ArrayList<Card> = arrayListOf(pigMonster, rabbitMonster)

        var constructorParams: MutableMap<String, KParameter> = mutableMapOf()
        kClass.primaryConstructor!!.parameters.forEach { constructorParams[it.name.toString()] = it }

        var createdObject: Any = kClass.primaryConstructor!!.callBy(mapOf())

        var allVariables: MutableMap<String, Any?> = getAllVariables(kClass, createdObject)

        assertTrue(allVariables["empty"] as Boolean)

        var cardListCards: ArrayList<Card> = allVariables["cards"] as ArrayList<Card>
        assertTrue(cardListCards.isEmpty())

        createdObject = kClass.primaryConstructor!!.callBy(
            mapOf(
                constructorParams["empty"]!! to false,
                constructorParams["cards"]!! to listOfCards
            ) as Map<KParameter, Any>
        )

        allVariables = getAllVariables(kClass, createdObject)

        assertFalse(allVariables["empty"] as Boolean)
        cardListCards = allVariables["cards"] as ArrayList<Card>
        assertEquals(2, cardListCards.size)
        assertTrue(cardListCards.containsAll(listOfCards))
        listOfCards.clear()
        assertTrue(cardListCards.isNotEmpty())
    }

    private fun getAllVariables(kClass: KClass<*>, createdObject: Any): MutableMap<String, Any?> {
        var allVariables: MutableMap<String, Any?> = mutableMapOf()
        kClass.superclasses.first().memberProperties.forEach {
            allVariables[it.name] = it.getter.call(createdObject)
        }
        return allVariables
    }
}