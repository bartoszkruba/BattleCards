package prototype

import java.nio.file.Path

class InitCards(private val cardLoader: CardLoader = CardLoader()) {
    private var counter = 1

    fun initCards() {
        Path.of("json").toAbsolutePath().toFile().mkdir()
        Path.of("json", "decks").toAbsolutePath().toFile().mkdir()

        println("Deleting old cards...")
        val cardsToDelete = ArrayList<Int>()
        cardLoader.loadCards().forEach { cardsToDelete.add(it.id) }
        cardLoader.deleteCards(cardsToDelete)

        println("Creating new cards...")
        val cards = ArrayList<CardPrototype>()
        cards.add(newMonster("Ogre", 3, 7))
        cards.add(newMonster("Spider", 4, 4))
        cards.add(newMonster("Skeleton", 1, 9))
        cards.add(newMonster("Wolf", 3, 3))
        cards.add(newMonster("Orc", 8, 4))
        cards.add(newMonster("Troll", 6, 6))
        cards.add(newMonster("Dragon", 10, 10))
        cards.add(newMonster("Wyvern", 9, 1))
        cards.add(newMonster("Gnome", 1, 1))
        cards.add(newMonster("Gryphon", 5, 9))
        cards.add(newMonster("Goblin", 2, 2))
        cards.add(newMonster("Rat", 1, 9))
        cards.add(newMonster("Dwarf", 9, 4))
        cards.add(newMonster("Elf", 8, 6))
        cards.add(newMonster("Fel Orc", 6, 10))
        cards.add(newMonster("Imp", 1, 3))
        cards.add(newMonster("Succub", 9, 6))
        cards.add(newMonster("Felhound", 4, 8))
        cards.add(newMonster("Void Cat", 1, 3))
        cards.add(newMonster("Souleater", 7, 7))
        cards.add(newMonster("Fel Rat", 2, 2))
        cards.add(newMonster("Fire Ogre", 2, 10))
        cards.add(newMonster("Dark Pig", 8, 5))
        cards.add(newMonster("Abyss Imp", 2, 8))
        cards.add(newSpell("Fireball", "Deals ${Settings.FIREBALL_DAMAGE} damage to enemy monster."))
        cards.add(newSpell("Heal", "Adds ${Settings.HEAL_VALUE} health points to friendly monster."))
        cards.add(newSpell("Void Hole", "Removes all monsters from board."))

        println("Saving new cards...")
        cardLoader.saveCards(cards)

        println("\nAdding new deck...")

        val deckPrototypeOne = DeckPrototype("Standard")
        repeat(2) {
            for (i in 0 until 15) {
                deckPrototypeOne.addCard(cards[i])
            }
        }

        cardLoader.saveDeck(deckPrototypeOne)
        cardLoader.loadDeck("Standard")
        println("Added new deck: Standard\n")

        val deckPrototypeTwo = DeckPrototype("Demons")

        repeat(3) {
            for (i in 14 until 24) {
                deckPrototypeTwo.addCard(cards[i])
            }
        }

        cardLoader.saveDeck(deckPrototypeTwo)
        cardLoader.loadDeck("Demons")
        println("Added new deck: Demons\n")

        val deckPrototypeThree = DeckPrototype("Rats")

        repeat(15) {
            deckPrototypeThree.addCard(cards[11])
            deckPrototypeThree.addCard(cards[20])
        }

        cardLoader.saveDeck(deckPrototypeThree)
        cardLoader.loadDeck("Rats")
        println("Added new deck: Rats\n")

        val deckPrototypeFour = DeckPrototype("Spells")

        repeat(2) {
            for (i in 0 until 9) {
                deckPrototypeFour.addCard(cards[i])
            }
        }

        repeat(6) {
            for (i in 24 until 26) {
                deckPrototypeFour.addCard(cards[i])
            }
        }

        cardLoader.saveDeck(deckPrototypeFour)
        cardLoader.loadDeck("Spells")
        println("Added new deck: Spells\n")

        println("\nDone!")
    }

    private fun newMonster(name: String, baseHealth: Int, baseAttack: Int): MonsterPrototype {
        val monster = MonsterPrototype(
            id = counter++,
            name = name,
            baseHealth = baseHealth,
            baseAttack = baseAttack
        )

        println("Created monster: $monster")

        return monster
    }

    private fun newSpell(name: String, description: String): CardPrototype {
        val spell = SpellPrototype(
            id = counter++,
            name = name,
            description = description
        )

        println("Created spell: $spell")

        return spell
    }
}

fun main() {
    InitCards().initCards()
}

