import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class MonsterTest {

    @Test
    fun ConstructorTest() {
        constructorWithNoArgument();
        constructorWithArgument();
    }

    fun constructorWithNoArgument() {
        var monster = Monster()
        var monster2 = Monster()
        assertNotEquals(monster.cardId, monster2.cardId)
        assertNotNull(monster)
        assertNotNull(monster2)
        assertNotSame(monster2, monster)
    }

    fun constructorWithArgument(){
        var monster = Monster("Ogre tok",6,4);
        var monster2 = Monster("Ogre",6,4);
        assertNotSame(monster2, monster)
        assertNotEquals(monster2.cardId, monster.cardId)
        assertEquals("Ogre", monster2.name)
        assertEquals(CardType.MONSTER, monster2.type)
        assertEquals(6, monster2.attack)
        assertEquals(4, monster2.health)
    }

    @Test
    fun takeDamge() {
        var monster1 = Monster("Ogre", 6, 3);
        var monster2 = Monster("Ogre", 7,4);

        var a = monster2.takeDamge(monster1)
        assertTrue(a)

        var s = monster2.takeDamge(monster1)
        assertFalse(s)
    }

    @Test
    fun isDead() {
        var monster1 = Monster("Ogre",4, 3);
        var monster2 = Monster("Ogre",7,9);

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertTrue(monster2.isDead())
    }

    @Test
    fun toStringTest() {
        val wolfCard = Monster("Wolf", 3, 6).toString()
        val wolfCardTest = """
             ___     
            |   |    
            |   |    
          3 |___| 6  
            Wolf     
        """.trimIndent()

        assertEquals(wolfCardTest, wolfCard, "The toString doesn't match")

        val gnarlCard = Monster("Gnarl", 8, 5).toString()
        val gnarlCardTest = """
             ___     
            |   |    
            |   |    
          8 |___| 5  
            Gnarl    
        """.trimIndent()

        assertEquals(gnarlCardTest, gnarlCard, "The toString doesn't match")

        val skeletonCard = Monster("Skeleton",10, 10).toString()
        val skeletonCardTest = """
             ___     
            |   |    
            |   |    
          10|___|10  
          Skeleton   
        """.trimIndent()

        assertEquals(skeletonCardTest, skeletonCard, "The toString doesn't match")
    }
}