package models

import Card
import Monster
import utilities.Utils

class Field(
    cards: ArrayList<Card> = ArrayList()
) : CardList(cards.size == 0, cards, Settings.FIELD_SIZE) {

    fun wakeUpMonsters(){
        cards.forEach{
            if (it is Monster){
                it.sleeping = false
            }
        }
    }

    override fun addCard(card: Card): Boolean {
        if (cards.size < maxSize) {
            for (c in cards) {
                if (c.cardId == card.cardId) {
                    return false
                }
            }

            val copied: Monster = Utils.clone(card) as Monster
            empty = false
            copied.sleeping = true
            return cards.add(copied)
        }
        return false
    }

    fun allCardsAreSleeping():Boolean{
        var sleepingCards:Int = 0
        cards.forEach {
            if (it is Monster && it.sleeping){
                sleepingCards++
            }else{
                return false
            }
        }
        return sleepingCards > 0
    }
}