import java.util.*

class Monster : Card {
    override var name: String
    override var cardId: UUID
    var cardType: CardType
    var attack: Int
    var health: Int

    constructor() : super("Wolf", CardType.MONSTER, UUID.randomUUID()){
        this.name = "Wolf"
        this.cardId = UUID.randomUUID()
        this.cardType =  CardType.MONSTER
        this.attack = 7;
        this.health = 5;
    }

    constructor(name:String, attack:Int, health:Int) : super(name, CardType.MONSTER, UUID.randomUUID()){
        val regex = Regex("^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$")
        if(regex.matches(name) && attack > 0 && attack < 11  && health > 0 && health < 11) {
            this.name = name;
            this.cardId = UUID.randomUUID()
            this.cardType = CardType.MONSTER
            this.attack = attack;
            this.health = health;
        }else{throw RuntimeException("Not valid input")}
    }

    fun takeDamge(card: Monster): Boolean {
        if (this.health <= 0) {
            return false
        } else {
            this.health = this.health - card.attack
            return true
        }
    }

    fun isDead(): Boolean {
        if (this.health <= 0) { return true }
        return false
    }
}