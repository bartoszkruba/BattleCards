package models

import Card
import Monster

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
}