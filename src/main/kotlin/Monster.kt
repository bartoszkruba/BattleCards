import java.util.*
import kotlin.math.floor

class Monster : Card {
    override var name: String
    override var cardId: UUID
    var cardType: CardType
    var attack: Int
    var health: Int
    var sleeping: Boolean = true

    constructor() : super("Wolf", CardType.MONSTER, UUID.randomUUID()){
        this.name = "Wolf"
        this.cardId = UUID.randomUUID()
        this.cardType =  CardType.MONSTER
        this.attack = 7;
        this.health = 5;
    }

    constructor(name:String, attack:Int, health:Int) : super(name, CardType.MONSTER, UUID.randomUUID()){
        val regex = Regex("^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$")
        if(name.length in 1..9 && regex.matches(name) && attack in 1..10  && health in 1..10) {
            this.name = name;
            this.cardId = UUID.randomUUID()
            this.cardType = CardType.MONSTER
            this.attack = attack;
            this.health = health;
        }else{throw RuntimeException("Invalid properties of object")}
    }

    fun takeDamge(card: Monster): Boolean {
        return if (this.health <= 0) {
            false
        } else {
            this.health = this.health - card.attack
            true
        }
    }

    fun isDead() = this.health <= 0

    override fun toString(): String {
        var atk = if(attack > 9) "${attack}" else "${attack} "
        var hp = if(health > 9) "${health}" else " ${health}"
        hp = if(health <= 0) " 0" else hp
        atk = "${Settings.ANSI_BLUE}${atk}${Settings.ANSI_RESET}"
        hp = "${Settings.ANSI_RED}${hp}${Settings.ANSI_RESET}"
        var sb = StringBuilder()
        repeat((4 - floor(name.length * 0.5)).toInt()) { sb.append(" ") }
        var cardName = "$sb${name}"
        sb.clear()
        repeat(11 - cardName.length) { sb.append(" ") }
        cardName += sb
        cardName = "${Settings.ANSI_GREEN}${cardName}${Settings.ANSI_RESET}"

        return """
             ___     
            |   |    
            |   |    
          $atk|___|$hp  
          $cardName
        """.trimIndent()
    }
}