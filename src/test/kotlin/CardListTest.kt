import models.CardList
import models.Deck
import models.Hand
import models.Field
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

internal class CardListTest {

    companion object {
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MAX_ATTACK = Settings.MAX_DAMAGE
    }

    @Test
    internal fun addCardTest() {
        var ogreCard = Monster("Ogre", MAX_ATTACK, MAX_HEALTH)
        var wolfCard = Monster("Wolf", MAX_ATTACK, MAX_HEALTH)
        var deck = Deck()

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
            var wolfCard: Monster = Monster("Wolf", MAX_ATTACK, MAX_HEALTH)
            if (i <= maxSize) assertTrue(clazz.addCard(wolfCard), "Should return true ass we are allowed to add cards")
            else assertFalse(
                clazz.addCard(wolfCard),
                "Should return false because we have added more cards than we are allowed to"
            )
        }
    }

    @Test
    internal fun cardsInListTest() {
        var pigMonster: Monster = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        var rabbitMonster: Monster = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
        var deck: Deck = Deck(arrayListOf(pigMonster, rabbitMonster))

        var deckClass = deck::class
        var deckClassVariables = getAllVariables(deckClass, deck)
        var originalDeckInClass = deckClassVariables["cards"] as ArrayList<Card>
        assertTrue(deck.cardsInList() === originalDeckInClass, "Returned list is not a reference of original list")
        assertTrue(
            deck.cardsInList()[0] === originalDeckInClass[0],
            "Returned objects in list is not a reference of the original object"
        )
        assertTrue(
            deck.cardsInList().containsAll(originalDeckInClass),
            "The returned list doesn't contain the objects from the original list"
        )
    }

    @Test
    internal fun removeCardTest() {
        var pigMonster: Monster = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        var rabbitMonster: Monster = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
        var deck: Deck = Deck(arrayListOf(pigMonster))

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

        deck = Deck(arrayListOf(rabbitMonster, pigMonster))

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

    private fun maxCardsInListConstructorTest(clazz: CardList, maxSize: Int) {

    }

    private fun cardListConstructorTest(kClass: KClass<*>) {
        var pigMonster: Card = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        var rabbitMonster: Card = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
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
//                constructorParams["empty"]!! to false,
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

        when (kClass.simpleName) {
            "Deck" -> {
                try {
                    Deck(getToLargeCardList(Settings.DECK_SIZE))
                    assertTrue(false, "Should throw IllegalArgumentException")
                } catch (err: IllegalArgumentException) {
                }
            }
            "Hand" -> {
                try {
                    Hand(getToLargeCardList(Settings.HAND_SIZE))
                    assertTrue(false, "Should throw IllegalArgumentException")
                } catch (err: IllegalArgumentException) {
                }
            }
            "Field" -> {
                try {
                    Field(getToLargeCardList(Settings.FIELD_SIZE))
                    assertTrue(false, "Should throw IllegalArgumentException")
                } catch (err: IllegalArgumentException) {
                }
            }
        }
    }

    private fun getToLargeCardList(maxSize: Int): ArrayList<Card> {
        var toLargeCardList: ArrayList<Card> = arrayListOf()
        for (i in 1..maxSize + 1) {
            toLargeCardList.add(Monster("monster", MAX_ATTACK, MAX_HEALTH))
        }
        return toLargeCardList
    }

    private fun getAllVariables(kClass: KClass<*>, createdObject: Any): MutableMap<String, Any?> {
        var allVariables: MutableMap<String, Any?> = mutableMapOf()
        kClass.superclasses.first().memberProperties.forEach {
            it.getter.isAccessible = true
            allVariables[it.name] = it.getter.call(createdObject)
        }
        return allVariables
    }

    @Test
    fun toStringTest() {
        var atk1 = "${Settings.ANSI_BLUE}4 ${Settings.ANSI_RESET}"
        var atk2 = "${Settings.ANSI_BLUE}1 ${Settings.ANSI_RESET}"
        var atk3 = "${Settings.ANSI_BLUE}3 ${Settings.ANSI_RESET}"
        var atk4 = "${Settings.ANSI_BLUE}2 ${Settings.ANSI_RESET}"
        var atk5 = "${Settings.ANSI_BLUE}1 ${Settings.ANSI_RESET}"

        var hp1 = "${Settings.ANSI_RED} 7${Settings.ANSI_RESET}"
        var hp2 = "${Settings.ANSI_RED} 3${Settings.ANSI_RESET}"
        var hp3 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"
        var hp4 = "${Settings.ANSI_RED} 2${Settings.ANSI_RESET}"
        var hp5 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"

        var name1 = "${Settings.ANSI_GREEN}  Ogre     ${Settings.ANSI_RESET}"
        var name2 = "${Settings.ANSI_GREEN}  Wolf     ${Settings.ANSI_RESET}"
        var name3 = "${Settings.ANSI_GREEN} Ranger    ${Settings.ANSI_RESET}"
        var name4 = "${Settings.ANSI_GREEN}  Slime    ${Settings.ANSI_RESET}"
        var name5 = "${Settings.ANSI_GREEN} Murloc    ${Settings.ANSI_RESET}"

        val fieldPattern = """
               ___        ___        ___        ___        ___     
              |   |      |   |      |   |      |   |      |   |    
              | 1 |      | 2 |      | 3 |      | 4 |      | 5 |    
            $atk1|___|$hp1  $atk2|___|$hp2  $atk3|___|$hp3  $atk4|___|$hp4  $atk5|___|$hp5  
            $name1$name2$name3$name4$name5
        """.trimIndent()

        val field = Field(
            arrayListOf(
                Monster("Ogre", 4, 7),
                Monster("Wolf", 1, 3),
                Monster("Ranger", 3, 4),
                Monster("Slime", 2, 2),
                Monster("Murloc", 1, 4)
            )
        ).toString()

        assertEquals(fieldPattern, field, "Field toString doesn't match pattern")

        atk1 = "${Settings.ANSI_BLUE}2 ${Settings.ANSI_RESET}"
        atk2 = "${Settings.ANSI_BLUE}1 ${Settings.ANSI_RESET}"
        atk3 = "${Settings.ANSI_BLUE}7 ${Settings.ANSI_RESET}"
        atk4 = "${Settings.ANSI_BLUE}5 ${Settings.ANSI_RESET}"
        atk5 = "${Settings.ANSI_BLUE}1 ${Settings.ANSI_RESET}"

        hp1 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"
        hp2 = "${Settings.ANSI_RED} 3${Settings.ANSI_RESET}"
        hp3 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"
        hp4 = "${Settings.ANSI_RED} 9${Settings.ANSI_RESET}"
        hp5 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"

        name1 = "${Settings.ANSI_GREEN}  Gnarl    ${Settings.ANSI_RESET}"
        name2 = "${Settings.ANSI_GREEN}  Wolf     ${Settings.ANSI_RESET}"
        name3 = "${Settings.ANSI_GREEN}Skeleton   ${Settings.ANSI_RESET}"
        name4 = "${Settings.ANSI_GREEN}WereWolf   ${Settings.ANSI_RESET}"
        name5 = "${Settings.ANSI_GREEN} Murloc    ${Settings.ANSI_RESET}"

        val handPattern = """
               ___        ___        ___        ___        ___     
              |   |      |   |      |   |      |   |      |   |    
              | 1 |      | 2 |      | 3 |      | 4 |      | 5 |    
            $atk1|___|$hp1  $atk2|___|$hp2  $atk3|___|$hp3  $atk4|___|$hp4  $atk5|___|$hp5  
            $name1$name2$name3$name4$name5
        """.trimIndent()

        val hand = Hand(
            arrayListOf(
                Monster("Gnarl", 2, 4),
                Monster("Wolf", 1, 3),
                Monster("Skeleton", 7, 4),
                Monster("WereWolf", 5, 9),
                Monster("Murloc", 1, 4)
            )
        ).toString()

        assertEquals(handPattern, hand, "Hand toString doesn't match pattern")
    }

    @Test
    fun cardToStringTest() {
        val field = Field()

        val wolfCard = Monster("Wolf", 3, 6)

        var atk = "${Settings.ANSI_BLUE}3 ${Settings.ANSI_RESET}"
        var hp = "${Settings.ANSI_RED} 6${Settings.ANSI_RESET}"
        var name = "${Settings.ANSI_GREEN}  Wolf     ${Settings.ANSI_RESET}"

        val wolfCardTest = """
            ___     
           |   |    
           | 1 |    
         $atk|___|$hp  
         $name
        """.trimIndent()

        assertEquals(wolfCardTest, field.cardToString(wolfCard, 0), "The toString doesn't match")

        val gnarlCard = Monster("Gnarl", 8, 5)

        atk = "${Settings.ANSI_BLUE}8 ${Settings.ANSI_RESET}"
        hp = "${Settings.ANSI_RED} 5${Settings.ANSI_RESET}"
        name = "${Settings.ANSI_GREEN}  Gnarl    ${Settings.ANSI_RESET}"

        val gnarlCardTest = """
            ___     
           |   |    
           | 3 |    
         $atk|___|$hp  
         $name
        """.trimIndent()

        assertEquals(gnarlCardTest, field.cardToString(gnarlCard, 2), "The toString doesn't match")

        val skeletonCard = Monster("Skeleton",10, 10)

        atk = "${Settings.ANSI_BLUE}10${Settings.ANSI_RESET}"
        hp = "${Settings.ANSI_RED}10${Settings.ANSI_RESET}"
        name = "${Settings.ANSI_GREEN}Skeleton   ${Settings.ANSI_RESET}"

        val skeletonCardTest = """
            ___     
           |   |    
           | 5 |    
         $atk|___|$hp  
         $name
        """.trimIndent()

        assertEquals(skeletonCardTest, field.cardToString(skeletonCard, 4), "The toString doesn't match")
    }
}