package models

class Player(
    name: String,
    var deck: Deck = Deck(),
    var hand: Hand = Hand(),
    var field: Field = Field()
) : User(name) {
    var mana: Int = Settings.PLAYER_MANA
}

