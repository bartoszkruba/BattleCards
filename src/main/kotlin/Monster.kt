import java.util.*
import kotlin.math.floor

class Monster : Card {
    override var name: String
    var attack: Int
    var health: Int
    var sleeping: Boolean = false

    constructor() : super("Wolf", CardType.MONSTER, UUID.randomUUID()){
        this.name = "Wolf"
        this.attack = 7
        this.health = 5
    }

    constructor(name:String, attack:Int, health:Int) : super(name, CardType.MONSTER, UUID.randomUUID()){
        val regex = Regex("^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$")
        if(name.length in 1..9 && regex.matches(name) && attack in 1..10  && health in 1..10) {
            this.name = name
            this.attack = attack
            this.health = health
        }else{throw RuntimeException("Invalid properties of object")}
    }

    fun takeDamage(card: Monster): Boolean {
        return if (this.health <= 0) {
            false
        } else {
            this.health = this.health - card.attack
            true
        }
    }

    fun takeDamage(damage: Int){
        this.health -= damage
    }

    fun isDead() = this.health <= 0

    override fun toString(): String {
        var atk: String = if(attack > 9) "$attack" else "$attack "
        var hp: String = if(health > 9) "$health" else " $health"
        hp = if(health <= 0) " 0" else hp
        atk =   (if(sleeping) Settings.ANSI_WHITE else Settings.ANSI_BLUE) +
                atk +
                if(sleeping) "" else Settings.ANSI_RESET

        hp =    (if(sleeping) Settings.ANSI_WHITE else Settings.ANSI_RED) +
                hp +
                Settings.ANSI_RESET
        val sb = StringBuilder()
        repeat((4 - floor(name.length * 0.5)).toInt()) { sb.append(" ") }
        var cardName = "$sb${name}"
        sb.clear()
        repeat(11 - cardName.length) { sb.append(" ") }
        cardName += sb
        cardName = (if(sleeping) Settings.ANSI_WHITE else Settings.ANSI_GREEN) +
                   cardName +
                   Settings.ANSI_RESET
        val sleepStart = if(sleeping) Settings.ANSI_WHITE else Settings.ANSI_RESET
        val sleepEnd = Settings.ANSI_RESET

        val lines: Array<String> = arrayOf(
              "$sleepStart   ___     $sleepEnd",
              "$sleepStart  |   |    $sleepEnd",
              "$sleepStart  |   |    $sleepEnd",
                       "$atk|___|$hp  ",
                           cardName
        )

        return lines.joinToString("\n")
    }
}