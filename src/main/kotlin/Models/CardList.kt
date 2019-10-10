package Models

abstract class CardList{

    private var empty:Boolean
    private var cards:List<Card>

    constructor(){
        this.empty = true
        this.cards = emptyList()
    }

    constructor(empty:Boolean,cards:List<Card>){
        this.empty = empty
        this.cards = cards
    }

    fun cardsInList():List<Card> {
        return cards
    }

    fun addCard(card:Card):Boolean{
        return false
    }

    fun removeCard(card:Card):Card{
        return null
    }
}