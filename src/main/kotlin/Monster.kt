class Monster (name: String = "something",
               type: CardType = CardType.MONSTER,
               cardId: Int = 546,
               var attack: Int = 7,
               var health: Int = 5) : Card(name, type, cardId){

    fun takeDamge(card: Monster): Boolean {
        if(this.health < 0){return false}
        else {
            this.health = this.health - card.attack
            return true
        }
    }

    fun isDead() : Boolean {
        return false
    }
}