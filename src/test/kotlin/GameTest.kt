import factory.DeckFactory
import models.Deck
import models.Field
import models.Hand
import models.Player

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import prototype.DeckPrototype
import prototype.MonsterPrototype
import utilities.Utils
import kotlin.collections.ArrayList
import kotlin.random.Random

internal class GameTest {
    private lateinit var player1: Player
    private lateinit var player2: Player

    companion object {
        const val MAX_ATTACK = Settings.MAX_DAMAGE
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val FIREBALL_DAMAGE = Settings.FIREBALL_DAMAGE
        const val PLAYER_MANA = Settings.PLAYER_MANA
    }

    @Test
    internal fun nextTurn() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.mana = 0
        game.blackPlayer.mana = 0
        game.whitePlayer.field = player1.field
        game.blackPlayer.field = player2.field
        game.whitePlayer.field.cardsInList().forEach {
            if (it is Monster) {
                it.sleeping = true
            }
        }
        game.blackPlayer.field.cardsInList().forEach {
            if (it is Monster) {
                it.sleeping = true
            }
        }
        checkSleeping(game, true)
        game.nextTurn()
        checkSleeping(game, false)
        assertEquals(2, game.turn, "Turn should have increased by one")
        assertEquals(Settings.PLAYER_MANA, game.whitePlayer.mana)
        assertEquals(Settings.PLAYER_MANA, game.blackPlayer.mana)
    }

    private fun checkSleeping(game: Game, shouldSleep: Boolean) {
        val errorMsg = if (shouldSleep) "Monster should be sleeping" else "Monster should not be sleeping"
        game.whitePlayer.field.cardsInList().forEach {
            if (it is Monster) {
                assertEquals(shouldSleep, it.sleeping, errorMsg)
            }
        }
        game.blackPlayer.field.cardsInList().forEach {
            if (it is Monster) {
                assertEquals(shouldSleep, it.sleeping, errorMsg)
            }
        }
    }


    @Test
    internal fun placeCardOnFieldTest() {
        createMockData()
        var game = Game(player1.deck, player2.deck, player1.name, player2.name)

        var index = 1
        for (i in 1..(Settings.FIELD_SIZE + 1) * 2) {
            game.whitePlayer.hand = Hand(arrayListOf(player1.deck.cardsInList()[i]))
            game.blackPlayer.hand = Hand(arrayListOf(player2.deck.cardsInList()[i]))

            val placedCard = game.currentPlayer().hand.cardsInList()[0]
            val result = game.placeCardOnField(game.currentPlayer().hand.cardsInList()[0])
            if (index > Settings.FIELD_SIZE) {
                assertFalse(result, "Should be false because field is full")
                assertEquals(1, game.currentPlayer().hand.size(), "Card should not have been removed from hand")
                assertFalse(
                    game.currentPlayer().field.cardsInList().contains(placedCard),
                    "Card should not have been placed on field"
                )
                assertEquals(Settings.PLAYER_MANA, game.currentPlayer().mana, "Mana should be unchanged")
            } else {
                assertTrue(result, "Should be true because field is not full")
                assertEquals(
                    index,
                    game.currentPlayer().field.cardsInList().size,
                    "Should be $index because card is added"
                )
                assertEquals(placedCard, game.currentPlayer().field.cardsInList()[index - 1], "Card should be the same")
                assertEquals(0, game.currentPlayer().hand.size(), "Card should have been removed from hand")
                assertEquals(Settings.PLAYER_MANA - 1, game.currentPlayer().mana, "Mana should have decreased")
            }

            game.nextTurn()
            if (i % 2 == 0) index++

        }

        game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.hand = Hand()
        assertFalse(
            game.placeCardOnField(player1.hand.cardsInList()[0]),
            "Should return false because no cards in hand"
        )
    }

    @Test
    fun getWinner() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.deck
        game.blackPlayer.deck
        game.whitePlayer.hand
        game.blackPlayer.hand
        game.whitePlayer.field
        game.blackPlayer.field
        assertNull(game.getWinner())

        game.whitePlayer.deck
        game.blackPlayer.deck = Deck()
        game.whitePlayer.hand
        game.blackPlayer.hand = Hand()
        game.whitePlayer.field
        game.blackPlayer.field = Field()
        assertEquals(game.whitePlayer, game.getWinner())
        assertNotEquals(game.blackPlayer, game.getWinner())
    }

    @Test
    internal fun drawCardFromDeckTest() {
        createMockData()
        var game = Game(player1.deck, player2.deck, player1.name, player2.name)

        var index = 1
        for (i in 1..(Settings.HAND_SIZE + 1) * 2) {
            val prevDeckSize = game.currentPlayer().deck.cardsInList().size
            val drawnCard = game.currentPlayer().deck.cardsInList()[0]
            val result = game.drawCardFromDeck()
            if (index > Settings.HAND_SIZE) {
                assertFalse(result)
                assertEquals(
                    prevDeckSize,
                    game.currentPlayer().deck.size(),
                    "Card should not have been removed from deck"
                )
                assertTrue(game.currentPlayer().deck.cardsInList().contains(drawnCard))
                assertFalse(game.currentPlayer().hand.cardsInList().contains(drawnCard))
                assertEquals(Settings.PLAYER_MANA, game.currentPlayer().mana, "Mana should be unchanged")
            } else {
                assertTrue(result)
                assertEquals(index, game.currentPlayer().hand.cardsInList().size)
                assertTrue(game.currentPlayer().hand.cardsInList().contains(drawnCard))
                assertFalse(game.currentPlayer().deck.cardsInList().contains(drawnCard))
                assertEquals(Settings.PLAYER_MANA - 1, game.currentPlayer().mana, "Mana should have decreased")
            }

            game.nextTurn()
            if (i % 2 == 0) index++
        }

        createMockData()
        game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.currentPlayer().deck = Deck()
        assertFalse(game.drawCardFromDeck(), "Should return false because no cards in hand")
    }


    @Test
    internal fun checkGameOverTest() {
        val deckPrototype = DeckPrototype("aaaa")
        repeat(Settings.DECK_SIZE) { deckPrototype.addCard(MonsterPrototype(1, "aaaa", 5, 5)) }
        for (i in 0..10000) {
            val game = Game(
                DeckFactory.createDeck(deckPrototype),
                DeckFactory.createDeck(deckPrototype),
                "player1",
                "player2"
            )
            game.whitePlayer.deck = Deck(getListWithRandomAmountOfCards(Settings.DECK_SIZE))
            game.whitePlayer.hand = Hand(getListWithRandomAmountOfCards(Settings.HAND_SIZE))
            game.whitePlayer.field = Field(getListWithRandomAmountOfCards(Settings.FIELD_SIZE))
            game.blackPlayer.deck = Deck(getListWithRandomAmountOfCards(Settings.DECK_SIZE))
            game.blackPlayer.hand = Hand(getListWithRandomAmountOfCards(Settings.HAND_SIZE))
            game.blackPlayer.field = Field(getListWithRandomAmountOfCards(Settings.FIELD_SIZE))
            var expectedResult: Boolean
            expectedResult =
                (game.whitePlayer.deck.size() == 0 && game.whitePlayer.field.size() == 0 && game.whitePlayer.hand.size() == 0)
                        || (game.blackPlayer.deck.size() == 0 && game.blackPlayer.field.size() == 0 && game.blackPlayer.hand.size() == 0)

            assertEquals(expectedResult, game.checkGameOver())
        }
    }

    @Test
    internal fun `checkGameOver() test, only spells left, white`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        repeat(wPlayer.deck.size()) { wPlayer.deck.drawCard() }

        repeat(Settings.HAND_SIZE) { wPlayer.hand.addCard(Spell("Fireball")) }
        repeat(Settings.DECK_SIZE) { wPlayer.deck.addCard(Spell("Heal")) }

        assertTrue(game.checkGameOver())
        assertEquals(game.blackPlayer, game.getWinner())
    }

    @Test
    internal fun `checkGameOver() test, only spells left, black`() {
        val game = createGameForTesting()

        val bPlayer = game.blackPlayer

        repeat(bPlayer.deck.size()) { bPlayer.deck.drawCard() }

        repeat(Settings.HAND_SIZE) { bPlayer.hand.addCard(Spell("Fireball")) }
        repeat(Settings.DECK_SIZE) { bPlayer.deck.addCard(Spell("Heal")) }

        assertTrue(game.checkGameOver())
        assertEquals(game.whitePlayer, game.getWinner())
    }

    private fun getListWithRandomAmountOfCards(maxSize: Int): ArrayList<Card> {
        val listOfCards: ArrayList<Card> = arrayListOf()
        for (i in 1..Random.nextInt(0, maxSize)) {
            listOfCards.add(Monster("Monster", 2, 5))
        }
        return listOfCards
    }

    @Test
    internal fun `gameOver(), test return true`() {
        val deckPrototype = DeckPrototype("aaaa")
        repeat(Settings.DECK_SIZE) { deckPrototype.addCard(MonsterPrototype(1, "aaaa", 5, 5)) }

        val game = Game(
            DeckFactory.createDeck(deckPrototype),
            DeckFactory.createDeck(deckPrototype),
            "player1",
            "player2"
        )

        game.whitePlayer.deck = Deck(ArrayList())
        game.whitePlayer.hand = Hand(ArrayList())
        game.whitePlayer.field = Field(ArrayList())

        assertTrue(game.checkGameOver())
    }

    @Test
    internal fun gameConstructorTest() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        assertEquals(1, game.turn)
        assertEquals("", game.status)
        assertEquals(player1.name, game.whitePlayer.name)
        assertEquals(player1.deck, game.whitePlayer.deck)
        assertEquals(player2.name, game.blackPlayer.name)
        assertEquals(player2.deck, game.blackPlayer.deck)
    }

    @Test
    internal fun `validMoves(), all moves valid`() {
        val game = createGameForTesting()

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        blackP.field.addCard(blackP.deck.drawCard()!!)
        whiteP.field.addCard(whiteP.deck.drawCard()!!)
        whiteP.hand.addCard(whiteP.deck.drawCard()!!)

        whiteP.field.wakeUpMonsters()

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach {
            generatedMoves.add(it.value)
        }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_DRAW_CARD, Settings.MENU_OPTION_ATTACK_MONSTER,
                Settings.MENU_OPTION_END_ROUND, Settings.MENU_OPTION_PLACE_CARD
            ), generatedMoves
        )
        assertEquals(4, generatedMoves.size)
    }

    @Test
    internal fun `validMoves(), only draw valid`() {
        val game = createGameForTesting()

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach { generatedMoves.add(it.value) }

        assertContains(arrayListOf(Settings.MENU_OPTION_DRAW_CARD, Settings.MENU_OPTION_END_ROUND), generatedMoves)
        assertEquals(2, generatedMoves.size)
    }

    @Test
    internal fun `validMoves(), only attack valid`() {
        val game = createGameForTesting()

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        blackP.field.addCard(blackP.deck.drawCard()!!)
        repeat(Settings.FIELD_SIZE) { whiteP.field.addCard(whiteP.deck.drawCard()!!) }
        repeat(Settings.HAND_SIZE) { whiteP.hand.addCard(whiteP.deck.drawCard()!!) }

        whiteP.field.wakeUpMonsters()

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach { generatedMoves.add(it.value) }

        assertContains(arrayListOf(Settings.MENU_OPTION_END_ROUND, Settings.MENU_OPTION_ATTACK_MONSTER), generatedMoves)
        assertEquals(2, generatedMoves.size)
    }

    @Test
    internal fun `validMoves, only place valid`() {
        val game = createGameForTesting()

        val whiteP = game.whitePlayer

        repeat(Settings.HAND_SIZE) { whiteP.hand.addCard(whiteP.deck.drawCard()!!) }

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach { generatedMoves.add(it.value) }

        assertContains(arrayListOf(Settings.MENU_OPTION_END_ROUND, Settings.MENU_OPTION_PLACE_CARD), generatedMoves)
        assertEquals(2, generatedMoves.size)
    }

    @Test
    internal fun `validMoves(), only place and attack valid`() {
        val game = createGameForTesting()

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        blackP.field.addCard(blackP.deck.drawCard()!!)
        repeat(1) { whiteP.field.addCard(whiteP.deck.drawCard()!!) }
        repeat(Settings.HAND_SIZE) { whiteP.hand.addCard(whiteP.deck.drawCard()!!) }

        whiteP.field.wakeUpMonsters()

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach { generatedMoves.add(it.value) }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_END_ROUND,
                Settings.MENU_OPTION_ATTACK_MONSTER,
                Settings.MENU_OPTION_PLACE_CARD
            ),
            generatedMoves
        )
        assertEquals(3, generatedMoves.size)
    }

    @Test
    internal fun `validMoves, only place and draw enabled`() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)

        val whiteP = game.whitePlayer

        whiteP.field = player1.field
        whiteP.hand = Hand(arrayListOf(player1.hand.cardsInList()[0], player1.hand.cardsInList()[1]))

        whiteP.field.removeCard(whiteP.field.cardsInList()[0])

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach {
            generatedMoves.add(it.value)
        }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_PLACE_CARD,
                Settings.MENU_OPTION_END_ROUND,
                Settings.MENU_OPTION_DRAW_CARD
            ),
            generatedMoves
        )
        assertEquals(3, generatedMoves.size)
    }

    @Test
    internal fun `validMoves, sleepingMonsters no attack`() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        whiteP.field = player1.field
        whiteP.hand = Hand(arrayListOf(player1.hand.cardsInList()[0], player1.hand.cardsInList()[1]))
        blackP.field = player2.field

        whiteP.field.cardsInList().forEach { if (it is Monster) it.sleeping = true }

        whiteP.field.removeCard(whiteP.field.cardsInList()[0])

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach {
            generatedMoves.add(it.value)
        }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_PLACE_CARD,
                Settings.MENU_OPTION_END_ROUND,
                Settings.MENU_OPTION_DRAW_CARD
            ),
            generatedMoves
        )
        assertEquals(3, generatedMoves.size)
    }

    @Test
    internal fun `validMoves, some sleeping monsters but not the hole field attack should be enabled`() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        whiteP.field = player1.field
        whiteP.hand = Hand(arrayListOf(player1.hand.cardsInList()[0], player1.hand.cardsInList()[1]))
        blackP.field = player2.field

        whiteP.field.wakeUpMonsters()

        var monsterToChange = whiteP.field.cardsInList()[0] as Monster
        monsterToChange.sleeping = true

        whiteP.field.removeCard(whiteP.field.cardsInList()[0])

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach {
            generatedMoves.add(it.value)
            println(it.value)
        }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_ATTACK_MONSTER,
                Settings.MENU_OPTION_PLACE_CARD,
                Settings.MENU_OPTION_END_ROUND,
                Settings.MENU_OPTION_DRAW_CARD
            ),
            generatedMoves
        )
        assertEquals(4, generatedMoves.size)
    }

    @Test
    internal fun `validMoves, only draw and attack valid`() {
        val game = createGameForTesting()

        val whiteP = game.whitePlayer
        val blackP = game.blackPlayer

        blackP.field.addCard(blackP.deck.drawCard()!!)
        repeat(Settings.FIELD_SIZE) { whiteP.field.addCard(whiteP.deck.drawCard()!!) }

        whiteP.field.wakeUpMonsters()

        val generatedMoves = ArrayList<String>()
        game.validMoves().forEach { generatedMoves.add(it.value) }

        assertContains(
            arrayListOf(
                Settings.MENU_OPTION_END_ROUND,
                Settings.MENU_OPTION_ATTACK_MONSTER,
                Settings.MENU_OPTION_DRAW_CARD
            ),
            generatedMoves
        )
        assertEquals(3, generatedMoves.size)
    }

    private fun assertContains(required: ArrayList<String>, result: ArrayList<String>) {
        assertTrue(required.containsAll(result))
    }

    @Test
    fun currentPlayerTest() {
        val deckPrototype = DeckPrototype("aaaa")

        repeat(Settings.DECK_SIZE) { deckPrototype.addCard(MonsterPrototype(1, "aaaaa", 5, 5)) }

        val game = Game(
            DeckFactory.createDeck(deckPrototype), DeckFactory.createDeck(deckPrototype),
            "player1", "player2"
        )
        val whitePlayer: Player = game.whitePlayer
        val blackPlayer: Player = game.blackPlayer

        assertTrue(game.turn % 2 != 0 && game.currentPlayer() == whitePlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 == 0 && game.currentPlayer() == blackPlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 != 0 && game.currentPlayer() == whitePlayer)
        game.nextTurn()
        assertTrue(game.turn % 2 == 0 && game.currentPlayer() == blackPlayer)
    }

    @Test
    internal fun attackMonsterTest() {
        createMockData()
        val game = Game(player1.deck, player2.deck, player1.name, player2.name)
        game.whitePlayer.field = player1.field
        game.blackPlayer.field = player2.field

        var index = 0
        do {
            val attacker: Monster = game.whitePlayer.field.cardsInList()[index] as Monster
            val getsAttacked: Monster = game.blackPlayer.field.cardsInList()[index] as Monster
            val getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertTrue(attacker.sleeping, "attacker should be sleeping")
            assertEquals(
                getsAttackedCopy.health - attacker.attack,
                getsAttacked.health,
                "Attacked card didn't loose right health"
            )
            assertNotEquals(
                getsAttacked.toString(),
                getsAttackedCopy.toString(),
                "Attacked card should have new health values"
            )
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
            index++
            assertEquals(Settings.PLAYER_MANA - index, game.whitePlayer.mana, "Mana should have decreased")
        } while (index < game.blackPlayer.field.size())

        assertEquals(3, game.blackPlayer.field.size(), "Dead cards wasn't removed from field")

        game.nextTurn()
        index = 0
        do {
            val attacker: Monster = game.blackPlayer.field.cardsInList()[index] as Monster
            val getsAttacked: Monster = game.whitePlayer.field.cardsInList()[index] as Monster
            val getsAttackedCopy: Monster = Utils.clone(getsAttacked) as Monster

            game.attackMonster(attacker, getsAttacked)
            assertTrue(attacker.sleeping, "Attacker should be sleeping")
            assertEquals(
                getsAttackedCopy.health - attacker.attack,
                getsAttacked.health,
                "Attacked card didn't loose right health"
            )
            assertNotEquals(
                getsAttacked.toString(),
                getsAttackedCopy.toString(),
                "Attacked card should have new health values"
            )
            assertTrue(getsAttacked.cardId == getsAttackedCopy.cardId, "Attacked card isn't the same after attack")
            index++
            assertEquals(Settings.PLAYER_MANA - index, game.blackPlayer.mana, "Mana should have decreased")
        } while (index < game.blackPlayer.field.cardsInList().size)

        assertEquals(4, game.whitePlayer.field.size(), "Dead cards wasn't removed from field")
    }

    private fun createMockData() {
        // reset players for mock data
        player1 = Player("player1")
        player2 = Player("player2")


        val players: Array<Player> = arrayOf(player1, player2)

        repeat(2) {
            var testCards: ArrayList<Monster> = generateCards()

            val deck = Deck()
            val hand = Hand()
            val field = Field()

            var index = 0
            repeat(Settings.HAND_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                hand.addCard(card)
            }
            index = 0
            repeat(Settings.DECK_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                deck.addCard(card)
                if (index > 4) {
                    index = 0
                    testCards = generateCards()
                }
            }
            index = 0
            repeat(Settings.FIELD_SIZE) {
                val card: Monster = Utils.clone(testCards[index++]) as Monster
                field.addCard(card)
            }
            players[it] = Player("Player$it", deck, hand, field)
        }

        player1 = players[0]
        player2 = players[1]
    }

    private fun generateCards(): ArrayList<Monster> {
        return arrayListOf(
            Monster("Ogre", 4, 7),
            Monster("Wolf", 3, 2),
            Monster("Ranger", 3, 4),
            Monster("Slime", 2, 2),
            Monster("Murloc", 1, 4)
        )
    }

    @Test
    internal fun `Shuffle on game start test`() {

        val cardsOne = ArrayList<Card>()
        val cardsTwo = ArrayList<Card>()

        repeat(10) {
            cardsOne.add(
                Monster(
                    name = "aaaaa",
                    health = 5,
                    attack = 5
                )
            )
            cardsOne.add(
                Monster(
                    name = "bbbbb",
                    health = 3,
                    attack = 3
                )
            )
            cardsOne.add(
                Monster(
                    name = "ccccc",
                    health = 6,
                    attack = 6
                )
            )
            cardsTwo.add(
                Monster(
                    name = "aaaaa",
                    health = 5,
                    attack = 5
                )
            )
            cardsTwo.add(
                Monster(
                    name = "bbbbb",
                    health = 3,
                    attack = 3
                )
            )
            cardsTwo.add(
                Monster(
                    name = "ccccc",
                    health = 6,
                    attack = 6
                )
            )
        }

        val game =
            Game(player1Deck = Deck(cardsOne), player2Deck = Deck(cardsTwo), player1Name = "aaa", player2Name = "bbb")

        assertFalse(listsExactlySame(game.whitePlayer.deck.cardsInList(), cardsOne))
        assertFalse(listsExactlySame(game.blackPlayer.deck.cardsInList(), cardsOne))
    }

    private fun listsExactlySame(listOne: List<Card>, listTwo: List<Card>): Boolean {
        for (i in listOne.indices) if (listOne[i] != listTwo[i]) return false
        return true
    }

    @Test
    internal fun `constructor test, white deck too short`() {
        val deckPrototypeOne = DeckPrototype("aaaa")
        repeat(Settings.DECK_SIZE - 1) {
            deckPrototypeOne.addCard(MonsterPrototype(1, "aaaaa", 5, 5))
        }

        val deckPrototypeTwo = DeckPrototype("bbbb")
        repeat(Settings.DECK_SIZE) {
            deckPrototypeTwo.addCard(MonsterPrototype(1, "aaaaa", 5, 5))
        }

        assertThrows(RuntimeException::class.java) {
            Game(
                DeckFactory.createDeck(deckPrototypeOne), DeckFactory.createDeck(deckPrototypeTwo),
                "aaaa", "bbbb"
            )
        }
    }

    @Test
    internal fun `constructor test, black deck too short`() {
        val deckPrototypeOne = DeckPrototype("aaaa")
        repeat(Settings.DECK_SIZE - 1) {
            deckPrototypeOne.addCard(MonsterPrototype(1, "aaaaa", 5, 5))
        }

        val deckPrototypeTwo = DeckPrototype("bbbb")
        repeat(Settings.DECK_SIZE) {
            deckPrototypeTwo.addCard(MonsterPrototype(1, "aaaaa", 5, 5))
        }

        assertThrows(RuntimeException::class.java) {
            Game(
                DeckFactory.createDeck(deckPrototypeTwo), DeckFactory.createDeck(deckPrototypeOne),
                "aaaa", "bbbb"
            )
        }
    }

    @Test
    internal fun `validMoves(), no "Attack Monster" if opponents field is empty`() {
        val game = createGameForTesting()
        val whiteP = game.whitePlayer

        whiteP.field.addCard(whiteP.deck.drawCard()!!)

        game.validMoves().entries.forEach {
            if (it.value == Settings.MENU_OPTION_ATTACK_MONSTER)
                throw RuntimeException(""" "${Settings.MENU_OPTION_ATTACK_MONSTER}" in validMoves """)
        }
    }

    @Test
    internal fun `validMoves(), no "Draw Card" if hand is already full`() {
        val game = createGameForTesting()
        val whiteP = game.whitePlayer

        repeat(Settings.HAND_SIZE) { whiteP.hand.addCard(whiteP.deck.drawCard()!!) }

        game.validMoves().entries.forEach {
            if (it.value == Settings.MENU_OPTION_DRAW_CARD)
                throw RuntimeException(""" "${Settings.MENU_OPTION_DRAW_CARD}" in validMoves """)
        }
    }

    @Test
    internal fun `validMoves(), no "Place Card" if field is already full`() {
        val game = createGameForTesting()
        val whiteP = game.whitePlayer
        repeat(Settings.FIELD_SIZE) { whiteP.field.addCard(whiteP.deck.drawCard()!!) }
        whiteP.hand.addCard(whiteP.deck.drawCard()!!)

        game.validMoves().entries.forEach {
            if (it.value == Settings.MENU_OPTION_PLACE_CARD)
                throw RuntimeException(""" "${Settings.MENU_OPTION_PLACE_CARD}" in validMoves """)
        }
    }

    @Test
    internal fun `validMoves(), no "End Turn" if there is no Mana left`() {
        val game = createGameForTesting()
        val whiteP = game.whitePlayer
        whiteP.mana = 0

        val validMoves = game.validMoves()
        assertEquals(1, validMoves.size)
        assertEquals(Settings.MENU_OPTION_END_ROUND, validMoves[1])
    }

    @Test
    internal fun `castFireball() goes right`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer
        val bPlayer = game.blackPlayer

        repeat(2) { wPlayer.hand.addCard(Spell("Fireball")) }
        bPlayer.field.addCard(bPlayer.deck.drawCard()!!)

        assertEquals(2, wPlayer.hand.size())
        assertEquals(1, bPlayer.field.size())

        game.castFireball(1, 1)

        assertEquals(1, wPlayer.hand.size())
        assertEquals(1, bPlayer.field.size())

        assertEquals(PLAYER_MANA - 1, wPlayer.mana)
        assertEquals(MAX_HEALTH - FIREBALL_DAMAGE, (bPlayer.field.cardsInList()[0] as Monster).health)

        game.castFireball(1, 1)

        assertEquals(PLAYER_MANA - 2, wPlayer.mana)
        assertEquals(0, wPlayer.hand.size())
        assertEquals(0, bPlayer.field.size())
    }

    @Test
    internal fun `castFireball(), invalid cardIndex`() {
        val game = createGameForTesting()

        val bPlayer = game.blackPlayer

        bPlayer.field.addCard(bPlayer.deck.drawCard()!!)

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 1)
        }

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 0)
        }

        assertThrows(RuntimeException::class.java) {
            game.castFireball(0, 1)
        }
    }

    @Test
    internal fun `castFireball(), invalid targetIndex`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.hand.addCard(Spell("Fireball"))

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 1)
        }

        assertThrows(RuntimeException::class.java) {
            game.castFireball(0, 1)
        }

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 0)
        }
    }

    @Test
    internal fun `castFireball, invalid spell`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer
        val bPlayer = game.blackPlayer

        repeat(2) { wPlayer.hand.addCard(Spell("dddd")) }
        bPlayer.field.addCard(bPlayer.deck.drawCard()!!)

        assertEquals(2, wPlayer.hand.size())
        assertEquals(1, bPlayer.field.size())

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 1)
        }
    }

    @Test
    internal fun `castFireball, monster instead of spell`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer
        val bPlayer = game.blackPlayer

        repeat(2) { wPlayer.hand.addCard(wPlayer.deck.drawCard()!!) }
        bPlayer.field.addCard(bPlayer.deck.drawCard()!!)

        assertEquals(2, wPlayer.hand.size())
        assertEquals(1, bPlayer.field.size())

        assertThrows(RuntimeException::class.java) {
            game.castFireball(1, 1)
        }
    }

    @Test
    internal fun `castHeal() goes right`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.hand.addCard(Spell("Heal"))
        wPlayer.field.addCard(wPlayer.deck.drawCard()!!)

        game.castHeal(1, 1)

        assertEquals(PLAYER_MANA - 1, wPlayer.mana)
        assertEquals(MAX_HEALTH + 5, (wPlayer.field.cardsInList()[0] as Monster).health)
    }

    @Test
    internal fun `castHeal(), invalid cardIndex`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.field.addCard(wPlayer.deck.drawCard()!!)

        assertThrows(RuntimeException::class.java) {
            game.castHeal(1, 1)
        }

        assertThrows(RuntimeException::class.java) {
            game.castHeal(0, 1)
        }

        wPlayer.hand.addCard(Spell("Heal"))

        assertThrows(RuntimeException::class.java) {
            game.castHeal(2, 1)
        }
    }

    @Test
    internal fun `castHeal(), invalid targetIndex`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.hand.addCard(Spell("Heal"))
        wPlayer.field.addCard(wPlayer.deck.drawCard()!!)

        assertThrows(RuntimeException::class.java) {
            game.castHeal(1, 2)
        }
    }

    @Test
    internal fun `castHeal(), invalid spell`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.hand.addCard(Spell("dd"))
        wPlayer.field.addCard(wPlayer.deck.drawCard()!!)

        assertThrows(RuntimeException::class.java) {
            game.castHeal(1, 1)
        }
    }

    @Test
    internal fun `castHeal(), monster instead of spell`() {
        val game = createGameForTesting()

        val wPlayer = game.whitePlayer

        wPlayer.hand.addCard(Spell("dd"))
        wPlayer.field.addCard(wPlayer.deck.drawCard()!!)

        assertThrows(RuntimeException::class.java) {
            game.castHeal(1, 1)
        }
    }

    fun createGameForTesting(): Game {
        val deckPrototype = DeckPrototype("aaaaaaaaa")
        val monsterOne = MonsterPrototype(name = "aaaaaa", baseAttack = MAX_ATTACK, baseHealth = MAX_HEALTH, id = 1)
        repeat(30) { deckPrototype.addCard(monsterOne) }

        return Game(
            player1Name = "aaaaaaa",
            player2Name = "bbbbbbbb",
            player1Deck = DeckFactory.createDeck(deckPrototype),
            player2Deck = DeckFactory.createDeck(deckPrototype)
        )
    }
}
