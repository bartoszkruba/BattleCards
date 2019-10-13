import java.util.*

class Monster : Card {
    override var name: String
    override var cardId: UUID
    var cardType: CardType
    var attack: Int = 7
    var health: Int = 5

    constructor() : super("Wolf", CardType.MONSTER, UUID.randomUUID()){
        this.name = "Wolf"
        this.cardId = UUID.randomUUID()
        this.cardType =  CardType.MONSTER
        this.attack = 7;
        this.health = 5;
    }

    constructor(name:String, attack:Int, health:Int) : super(name, CardType.MONSTER, UUID.randomUUID()){
        this.name = name;
        this.cardId = UUID.randomUUID()
        this.cardType =  CardType.MONSTER
        this.attack = attack;
        this.health = health;
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