package factory

import Card
import Monster
import Spell
import prototype.CardPrototype
import prototype.MonsterPrototype
import prototype.SpellPrototype

class CardFactory {

    companion object {
        fun createCard(card: CardPrototype): Card {
            return when (card.type) {
                CardType.MONSTER -> {
                    card as MonsterPrototype
                    Monster(name = card.name, health = card.baseHealth, attack = card.baseAttack)
                }
                CardType.SPEll -> {
                    card as SpellPrototype
                    Spell(name = card.name)
                }
            }
        }
    }
}

