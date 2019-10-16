import java.util.*
import kotlin.math.floor

class Monster (name: String = "something",
               type: CardType = CardType.MONSTER,
               cardId: UUID = UUID.randomUUID(),
               var attack: Int = 7,
               var health: Int = 5) : Card(name, type, cardId) {

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

    override fun toString(): String {
        val atk = if(attack > 9) "$attack" else "$attack "
        var hp = if(health > 9) "$health" else " $health"
        hp = if(health <= 0) " 0" else hp
        var spaces = ""
        for (i in 1..(4 - floor(name.length * 0.5)).toInt()){
            spaces += " "
        }
//        var sb = StringBuilder()
//        repeat((4 - floor(name.length * 0.5)).toInt()) { sb.append(" ") }
        var cardName = "$spaces$name"
//        sb.clear()

        spaces = ""
        for (i in 1..(11 - cardName.length)){
            spaces += " "
        }
//        repeat(11 - cardName.length) { sb.append(" ") }
        cardName += spaces

        return """
             ___     
            |   |    
            |   |    
          $atk|___|$hp  
          $cardName
        """.trimIndent()
    }
}