import models.CardList
import models.Deck
import models.Hand
import models.Field
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
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
        val ogreCard = Monster("Ogre", MAX_ATTACK, MAX_HEALTH)
        val wolfCard = Monster("Wolf", MAX_ATTACK, MAX_HEALTH)
        val deck = Deck()

        assertTrue(deck.addCard(ogreCard))
        var cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<*>
        assertEquals(1, cardsInList.size, "The card wasn't added to the list")
        assertEquals(cardsInList[0], ogreCard, "Added card doesn't match the card that was added")

        ogreCard.attack = 20
        val deckOgre: Monster = cardsInList[0] as Monster
        assertNotEquals(deckOgre.attack, ogreCard.attack, "Added card is not a copy of original object")

        assertTrue(deck.addCard(wolfCard))
        cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<*>
        assertEquals(2, cardsInList.size, "The card wasn't added to the list")
        assertEquals(cardsInList[1], wolfCard, "Added card doesn't match the card that was added")

        wolfCard.attack = 20
        val deckWolf: Monster = cardsInList[1] as Monster
        assertNotEquals(deckWolf.attack, wolfCard.attack, "Added card is not a copy of original object")

        assertFalse(deck.addCard(ogreCard), "Card with unique ID already exists")
        cardsInList = getAllVariables(deck::class, deck)["cards"] as ArrayList<*>
        assertEquals(2, cardsInList.size, "Card with unique ID that already exists was added")

        testMaxAddCard(Deck(), Settings.DECK_SIZE)
        testMaxAddCard(Hand(), Settings.HAND_SIZE)
        testMaxAddCard(Field(), Settings.FIELD_SIZE)
    }

    private fun testMaxAddCard(clazz: CardList, maxSize: Int) {
        for (i in 1..maxSize + 1) {
            val wolfCard = Monster("Wolf", MAX_ATTACK, MAX_HEALTH)
            if (i <= maxSize) assertTrue(clazz.addCard(wolfCard), "Should return true ass we are allowed to add cards")
            else assertFalse(
                clazz.addCard(wolfCard),
                "Should return false because we have added more cards than we are allowed to"
            )
        }
    }

    @Test
    internal fun cardsInListTest() {
        val pigMonster = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        val rabbitMonster = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
        val deck = Deck(arrayListOf(pigMonster, rabbitMonster))

        val deckClass = deck::class
        val deckClassVariables = getAllVariables(deckClass, deck)
        val originalDeckInClass = deckClassVariables["cards"] as ArrayList<*>
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
        val pigMonster = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        val rabbitMonster = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
        var deck = Deck(arrayListOf(pigMonster))

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
        val hand = Hand()
        assertEquals(Settings.HAND_SIZE, hand.maxSize)
    }

    @Test
    internal fun fieldConstructorTest() {
        cardListConstructorTest(Field::class)
        val field = Field()
        assertEquals(Settings.FIELD_SIZE, field.maxSize)
    }

    @Test
    internal fun deckConstructorTest() {
        cardListConstructorTest(Deck::class)
        val deck = Deck()
        assertEquals(Settings.DECK_SIZE, deck.maxSize)
    }

    private fun cardListConstructorTest(kClass: KClass<*>) {
        val pigMonster: Card = Monster("Pig", MAX_ATTACK, MAX_HEALTH)
        val rabbitMonster: Card = Monster("Rabbit", MAX_ATTACK, MAX_HEALTH)
        val listOfCards: ArrayList<Card> = arrayListOf(pigMonster, rabbitMonster)

        val constructorParams: MutableMap<String, KParameter> = mutableMapOf()
        kClass.primaryConstructor!!.parameters.forEach { constructorParams[it.name.toString()] = it }

        var createdObject: Any = kClass.primaryConstructor!!.callBy(mapOf())

        var allVariables: MutableMap<String, Any?> = getAllVariables(kClass, createdObject)

        assertTrue(allVariables["empty"] as Boolean)

        var cardListCards= allVariables["cards"] as ArrayList<*>
        assertTrue(cardListCards.isEmpty())

        createdObject = kClass.primaryConstructor!!.callBy(
            mapOf(
                constructorParams["cards"]!! to listOfCards
            ) as Map<KParameter, Any>
        )

        allVariables = getAllVariables(kClass, createdObject)

        assertFalse(allVariables["empty"] as Boolean)
        cardListCards = allVariables["cards"] as ArrayList<*>
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
        val toLargeCardList: ArrayList<Card> = arrayListOf()
        for (i in 1..maxSize + 1) {
            toLargeCardList.add(Monster("monster", MAX_ATTACK, MAX_HEALTH))
        }
        return toLargeCardList
    }

    private fun getAllVariables(kClass: KClass<*>, createdObject: Any): MutableMap<String, Any?> {
        val allVariables: MutableMap<String, Any?> = mutableMapOf()
        kClass.superclasses.first().memberProperties.forEach {
            it.getter.isAccessible = true
            allVariables[it.name] = it.getter.call(createdObject)
        }
        return allVariables
    }

    @Test
    fun toStringTest() {
        val sleepStart = Settings.ANSI_WHITE
        val sleepEnd = Settings.ANSI_RESET
        val wake = Settings.ANSI_RESET

        var atk1 = "${Settings.ANSI_BLUE}4 ${Settings.ANSI_RESET}"
        var atk2 = "${Settings.ANSI_WHITE}1 "
        var atk3 = "${Settings.ANSI_BLUE}3 ${Settings.ANSI_RESET}"
        var atk4 = "${Settings.ANSI_BLUE}2 ${Settings.ANSI_RESET}"
        var atk5 = "${Settings.ANSI_WHITE}1 "

        var hp1 = "${Settings.ANSI_RED} 7${Settings.ANSI_RESET}"
        var hp2 = "${Settings.ANSI_WHITE} 3${Settings.ANSI_RESET}"
        var hp3 = "${Settings.ANSI_RED} 4${Settings.ANSI_RESET}"
        var hp4 = "${Settings.ANSI_RED} 2${Settings.ANSI_RESET}"
        var hp5 = "${Settings.ANSI_WHITE} 4${Settings.ANSI_RESET}"

        var name1 = "${Settings.ANSI_GREEN}  Ogre     ${Settings.ANSI_RESET}"
        var name2 = "${Settings.ANSI_WHITE}  Wolf     ${Settings.ANSI_RESET}"
        var name3 = "${Settings.ANSI_GREEN} Ranger    ${Settings.ANSI_RESET}"
        var name4 = "${Settings.ANSI_GREEN}  Slime    ${Settings.ANSI_RESET}"
        var name5 = "${Settings.ANSI_WHITE} Murloc    ${Settings.ANSI_RESET}"

        val fieldPatternLines: Array<String> = arrayOf(
            "$wake   ___     $wake$sleepStart   ___     $sleepEnd$wake   ___     $wake$wake   ___     $wake$sleepStart   ___     $sleepEnd",
            "$wake  |   |    $wake$sleepStart  |   |    $sleepEnd$wake  |   |    $wake$wake  |   |    $wake$sleepStart  |   |    $sleepEnd",
            "$wake  | 1 |    $wake$sleepStart  | 2 |    $sleepEnd$wake  | 3 |    $wake$wake  | 4 |    $wake$sleepStart  | 5 |    $sleepEnd",
            "$atk1|___|$hp1  $atk2|___|$hp2  $atk3|___|$hp3  $atk4|___|$hp4  $atk5|___|$hp5  ",
            "$name1$name2$name3$name4$name5"
        )
        val fieldPattern = fieldPatternLines.joinToString("\n")

        val field = Field(
            arrayListOf(
                Monster("Ogre", 4, 7),
                Monster("Wolf", 1, 3),
                Monster("Ranger", 3, 4),
                Monster("Slime", 2, 2),
                Monster("Murloc", 1, 4)
            )
        )
        (field.cards[1] as Monster).sleeping = true
        (field.cards[4] as Monster).sleeping = true
        assertEquals(fieldPattern, field.toString(), "Field toString doesn't match pattern")

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

        val handPatternLines: Array<String> = arrayOf(
            "$wake   ___     $wake$wake   ___     $wake$wake   ___     $wake$wake   ___     $wake$wake   ___     $wake",
            "$wake  |   |    $wake$wake  |   |    $wake$wake  |   |    $wake$wake  |   |    $wake$wake  |   |    $wake",
            "$wake  | 1 |    $wake$wake  | 2 |    $wake$wake  | 3 |    $wake$wake  | 4 |    $wake$wake  | 5 |    $wake",
            "$atk1|___|$hp1  $atk2|___|$hp2  $atk3|___|$hp3  $atk4|___|$hp4  $atk5|___|$hp5  ",
            "$name1$name2$name3$name4$name5"
        )
        val handPattern = handPatternLines.joinToString("\n")

        val hand = Hand(
            arrayListOf(
                Monster("Gnarl", 2, 4),
                Monster("Wolf", 1, 3),
                Monster("Skeleton", 7, 4),
                Monster("WereWolf", 5, 9),
                Monster("Murloc", 1, 4)
            )
        )
        assertEquals(handPattern, hand.toString(), "Hand toString doesn't match pattern")
    }

    @Test
    fun cardToStringTest() {
        val field = Field()
        val sleepTestArray: Array<Boolean> = arrayOf(true, false)

        repeat(2) {
            val sleepTest = sleepTestArray[it]
            val sleepStart = if (sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RESET
            val sleepEnd = Settings.ANSI_RESET

            val wolfCard = Monster("Wolf", 3, 6)
            wolfCard.sleeping = sleepTest
            var atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}3 ${if(sleepTest) "" else Settings.ANSI_RESET}"
            var hp = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED} 6${Settings.ANSI_RESET}"
            var name = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}  Wolf     ${Settings.ANSI_RESET}"

            val wolfCardLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  | 1 |    $sleepEnd",
                "$atk|___|$hp  ",
                name
            )
            val wolfCardTest = wolfCardLines.joinToString("\n")

            assertEquals(wolfCardTest, field.cardToString(wolfCard, 0), "The toString doesn't match")

            val gnarlCard = Monster("Gnarl", 8, 5)
            gnarlCard.sleeping = sleepTest
            atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}8 ${if(sleepTest) "" else Settings.ANSI_RESET}"
            hp = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED} 5${Settings.ANSI_RESET}"
            name = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}  Gnarl    ${Settings.ANSI_RESET}"

            val gnarlCardTestLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  | 3 |    $sleepEnd",
                "$atk|___|$hp  ",
                name
            )
            val gnarlCardTest = gnarlCardTestLines.joinToString("\n")

            assertEquals(gnarlCardTest, field.cardToString(gnarlCard, 2), "The toString doesn't match")

            val skeletonCard = Monster("Skeleton", 10, 10)
            skeletonCard.sleeping = sleepTest
            atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}10${if(sleepTest) "" else Settings.ANSI_RESET}"
            hp = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED}10${Settings.ANSI_RESET}"
            name = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}Skeleton   ${Settings.ANSI_RESET}"

            val skeletonCardTestLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  | 5 |    $sleepEnd",
                "$atk|___|$hp  ",
                name
            )
            val skeletonCardTest = skeletonCardTestLines.joinToString("\n")

            assertEquals(skeletonCardTest, field.cardToString(skeletonCard, 4), "The toString doesn't match")
        }
    }
}