package prototype

import java.nio.file.Path

class InitCards {
    var counter = 1

    fun initCards() {
        Path.of("json").toAbsolutePath().toFile().mkdir()

        val cardLoader = CardLoader()
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
        cards.add(newMonster("Demon", 6, 10))

        val cardsToDelete = ArrayList<Int>()
        cardLoader.loadCards().forEach { cardsToDelete.add(it.id) }
        println(cardsToDelete)
        cardLoader.deleteCards(cardsToDelete)
        cardLoader.saveCards(cards)
    }

    private fun newMonster(name: String, baseHealth: Int, baseAttack: Int) = MonsterPrototype(
        id = counter++,
        name = name,
        baseHealth = baseHealth,
        baseAttack = baseAttack
    )

}

fun main() {
    InitCards().initCards()
}

