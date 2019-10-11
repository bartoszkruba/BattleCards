class Monster (name: String = "something",
               type: CardType = CardType.MONSTER,
               cardId: Int = 546,
               var attack: Int = 7,
               var health: Int = 5) : Card(name, type, cardId){

    fun takeDamge(card: Monster): Boolean {
        return false
    }

    fun isDead() : Boolean {
        return false
    }
}