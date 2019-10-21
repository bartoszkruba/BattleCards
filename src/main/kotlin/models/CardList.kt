package models

import Card
import Monster
import utilities.Utils
import kotlin.math.floor

abstract class CardList(var empty: Boolean, cards: ArrayList<Card>, var maxSize: Int) {
    var cards: ArrayList<Card> = ArrayList()

    init {
        require(cards.size <= maxSize) { "Card list is too big, ohh nooo!" }
        this.cards = cards.map { Utils.clone(it) as Card } as ArrayList<Card>
    }

    fun size(): Int{
        return cards.size
    }

    fun cardsInList(): ArrayList<Card> {
        return cards
    }

    fun addCard(card: Card): Boolean {
        if (cards.size < maxSize) {
            for (c in cards) {
                if (c.cardId.equals(card.cardId)) {
                    return false
                }
            }

            val copied: Card = Utils.clone(card) as Card
            empty = false
            return cards.add(copied)
        }
        return false
    }

    fun removeCard(card: Card): Card {
        val cardToRemove = cards.findLast { it.cardId == card.cardId }
        val indexToRemoveFrom = cards.indexOf(cardToRemove)
        if (indexToRemoveFrom != -1) {
            val removedCard: Card = cards.removeAt(indexToRemoveFrom);
            if (cards.size == 0) empty = true
            return removedCard
        }
        throw RuntimeException("Card cannot be removed, it doesn't exist")
    }

    override fun toString(): String {
        val lines: ArrayList<String> = arrayListOf("", "", "", "", "")

        for ((id, card) in cards.withIndex()) {
            val cardString = cardToString(card, id)
            for ((i, line) in cardString.split("\n").withIndex()) {
                lines[i] += line
            }
        }
        return lines.joinToString("\n")
    }

    fun cardToString(card: Card, id: Int): String {
        card as Monster
        var atk = if(card.attack > 9) "${card.attack}" else "${card.attack} "
        var hp = if(card.health > 9) "${card.health}" else " ${card.health}"
        hp = if(card.health <= 0) " 0" else hp
        atk =   (if(card.sleeping) Settings.ANSI_WHITE else Settings.ANSI_BLUE) +
                atk +
                if(card.sleeping) "" else Settings.ANSI_RESET

        hp =    (if(card.sleeping) Settings.ANSI_WHITE else Settings.ANSI_RED) +
                hp +
                Settings.ANSI_RESET
        val sb = StringBuilder()
        repeat((4 - floor(card.name.length * 0.5)).toInt()) { sb.append(" ") }
        var cardName = "$sb${card.name}"
        sb.clear()
        repeat(11 - cardName.length) { sb.append(" ") }
        cardName += sb
        cardName = (if(card.sleeping) Settings.ANSI_WHITE else Settings.ANSI_GREEN) +
                cardName +
                Settings.ANSI_RESET
        val sleepStart = if(card.sleeping) Settings.ANSI_WHITE else Settings.ANSI_RESET
        val sleepEnd = Settings.ANSI_RESET
        val ID: Int = id + 1

//        TODO: Fix icons
//        var sword = "${Settings.ANSI_BLUE}${"\t%s".format(Settings.ASCII_SWORD)}${Settings.ANSI_RESET}"
//        var heart = "${Settings.ANSI_RED}${Settings.ASCII_HEART}${Settings.ANSI_RESET}"

        val lines: Array<String> = arrayOf(
            "$sleepStart   ___     $sleepEnd",
            "$sleepStart  |   |    $sleepEnd",
           "$sleepStart  | $ID |    $sleepEnd",
                      "$atk|___|$hp  ",
                         cardName
        )

        return lines.joinToString("\n")
    }
}