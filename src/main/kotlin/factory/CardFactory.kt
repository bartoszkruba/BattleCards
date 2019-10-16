package factory

import Card
import Monster
import prototype.CardPrototype

class CardFactory() {

    companion object {
        fun createCard(card: CardPrototype): Card{
            return Monster()
        }
    }
}

