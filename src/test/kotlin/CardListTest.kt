import models.CardList
import models.Deck
import models.Hand
import models.Field
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

internal class CardListTest {

    @Test
    internal fun addCardTest() {
        var ogreCard: Monster = Monster("Ogre")
        var wolfCard: Monster = Monster("Wolf")
        var deck: Deck = Deck()

        assertTrue(deck.addCard(ogreCard))
        var cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<Card>
        assertEquals(1, cardsInList.size, "The card wasn't added to the list")
        assertEquals(cardsInList[0], ogreCard, "Added card doesn't match the card that was added")

        ogreCard.attack = 20
        var deckOgre: Monster = cardsInList[0] as Monster
        assertNotEquals(deckOgre.attack, ogreCard.attack, "Added card is not a copy of original object")

        assertTrue(deck.addCard(wolfCard))
        cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<Card>
        assertEquals(2, cardsInList.size, "The card wasn't added to the list")
        assertEquals(cardsInList[1], wolfCard, "Added card doesn't match the card that was added")

        wolfCard.attack = 20
        var deckWolf: Monster = cardsInList[1] as Monster
        assertNotEquals(deckWolf.attack, wolfCard.attack, "Added card is not a copy of original object")

        assertFalse(deck.addCard(ogreCard), "Card with unique ID already exists")
        cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<Card>
        assertEquals(2, cardsInList.size, "Card with unique ID that already exists was added")

        testMaxAddCard(Deck(), Settings.DECK_SIZE)
        testMaxAddCard(Hand(), Settings.HAND_SIZE)
        testMaxAddCard(Field(), Settings.FIELD_SIZE)
    }

    private fun testMaxAddCard(clazz: CardList, maxSize: Int) {
        for (i in 1..maxSize + 1) {
            var wolfCard: Monster = Monster("Wolf")
            if (i <= maxSize) assertTrue(clazz.addCard(wolfCard), "Should return true ass we are allowed to add cards")
            else assertFalse(
                clazz.addCard(wolfCard),
                "Should return false because we have added more cards than we are allowed to"
            )
        }
    }

    @Test
    internal fun cardsInListTest() {
        var pigMonster: Monster = Monster("Pig")
        var rabbitMonster: Monster = Monster("Rabbit")
        var deck: Deck = Deck(false, arrayListOf(pigMonster, rabbitMonster))

        var deckClass = deck::class
        var deckClassVariables = getAllVariables(deckClass, deck)
        var originalDeckInClass = deckClassVariables["cards"] as ArrayList<Card>
        assertTrue(deck.cardsInList() !== originalDeckInClass, "Returned list is not a copy of original list")
        assertTrue(
            deck.cardsInList()[0] !== originalDeckInClass[0],
            "Returned objects in list is not a copy of the original object"
        )
        assertTrue(
            deck.cardsInList().containsAll(originalDeckInClass),
            "The returned copy doesnt contain the objects from the original list"
        )
    }

    @Test
    internal fun removeCardTest() {
        var pigMonster: Monster = Monster("Pig")
        var rabbitMonster: Monster = Monster("Rabbit")
        var deck: Deck = Deck(false, arrayListOf(pigMonster))

        var removedCard: Card = Monster()

        try {
            removedCard = deck.removeCard(pigMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(
            pigMonster.cardId,
            removedCard.cardId,
            "The removed card doesn't match the card we wanted to remove"
        )
        assertEquals(0, deck.cardsInList().size, "The card did not get removed, a card still exists in the deck")
        assertEquals(true, deck.empty, "The boolean empty should be set to true because no cards exists")

        deck = Deck(false, arrayListOf(rabbitMonster, pigMonster))

        try {
            removedCard = deck.removeCard(rabbitMonster)
        } catch (error: Exception) {
            assertTrue(false, "Something went wrong when trying to remove a card")
        }

        assertEquals(
            rabbitMonster.cardId,
            removedCard.cardId,
            "The removed card doesn't match the card we wanted to remove"
        )
        assertEquals(1, deck.cardsInList().size, "The card did not get removed, card array size is not correct")
        assertEquals(pigMonster, deck.cardsInList()[0], "The card that should exist is not the on existing")

        try {
            deck.removeCard(rabbitMonster)
            assertTrue(false, "Trying to remove a card that does not exist,should throw runtimeException")
        } catch (error: RuntimeException) {
        }
    }

    @Test
    internal fun handConstructorTest() {
        cardListConstructorTest(Hand::class)
        var hand: Hand = Hand()
        assertEquals(Settings.HAND_SIZE, hand.maxSize)
    }

    @Test
    internal fun fieldConstructorTest() {
        cardListConstructorTest(Field::class)
        var field: Field = Field()
        assertEquals(Settings.FIELD_SIZE, field.maxSize)
    }

    @Test
    internal fun deckConstructorTest() {
        cardListConstructorTest(Deck::class)
        var deck: Deck = Deck()
        assertEquals(Settings.DECK_SIZE, deck.maxSize)
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
        assertTrue(pigMonster !== cardListCards[0])
        assertTrue(cardListCards.containsAll(listOfCards))
        listOfCards.clear()
        assertTrue(cardListCards.isNotEmpty())
    }

    private fun getAllVariables(kClass: KClass<*>, createdObject: Any): MutableMap<String, Any?> {
        var allVariables: MutableMap<String, Any?> = mutableMapOf()
        kClass.superclasses.first().memberProperties.forEach {
            it.getter.isAccessible = true
            allVariables[it.name] = it.getter.call(createdObject)
        }
        return allVariables
    }
}