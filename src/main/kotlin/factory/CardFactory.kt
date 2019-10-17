package factory

import Card
import Monster
import prototype.CardPrototype
import prototype.MonsterPrototype

class CardFactory() {

    companion object {
        fun createCard(card: CardPrototype): Card {
            return when (card.type) {
                CardType.MONSTER -> {
                    card as MonsterPrototype
                    Monster(name = card.name, health = card.baseHealth, attack = card.baseAttack)
                }
                CardType.SPEll -> {
                    throw RuntimeException("Not implemented yet")
                }
            }
        }
    }
}

