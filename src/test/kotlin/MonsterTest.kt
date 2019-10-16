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
        assertEquals(monster.health, monster2.health)
        assertNotNull(monster)
    }

    fun constructorWithArgument() {
        var monster1 = Monster("", CardType.MONSTER, UUID.randomUUID(), 6, 4);
        var monster = Monster("", CardType.MONSTER, UUID.randomUUID(), 6, 4);
        assertNotSame(monster, monster1)
        assertEquals("", monster.name)
        assertEquals(CardType.MONSTER, monster.type)
        assertEquals(6, monster.attack)
        assertEquals(4, monster.health)
    }

    @Test
    fun takeDamge() {
        var monster1 = Monster("", CardType.MONSTER, UUID.randomUUID(), 6, 3);
        var monster2 = Monster("", CardType.MONSTER, UUID.randomUUID(), 7, 4);

        var a = monster2.takeDamge(monster1)
        assertTrue(a)

        var s = monster2.takeDamge(monster1)
        assertFalse(s)
    }

    @Test
    fun isDead() {
        var monster1 = Monster("", CardType.MONSTER, UUID.randomUUID(), 4, 3);
        var monster2 = Monster("", CardType.MONSTER, UUID.randomUUID(), 7, 10);

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertTrue(monster2.isDead())
    }

    @Test
    fun toStringTest() {
        val wolfCard = Monster("Wolf", CardType.MONSTER, UUID.randomUUID(), 3, 6).toString()
        val wolfCardTest = """
             ___     
            |   |    
            |   |    
          3 |___| 6  
            Wolf     
        """.trimIndent()

        assertEquals(wolfCardTest, wolfCard, "The toString doesn't match")

        val gnarlCard = Monster("Gnarl", CardType.MONSTER, UUID.randomUUID(), 8, 5).toString()
        val gnarlCardTest = """
             ___     
            |   |    
            |   |    
          8 |___| 5  
            Gnarl    
        """.trimIndent()

        assertEquals(gnarlCardTest, gnarlCard, "The toString doesn't match")

        val skeletonCard = Monster("Skeleton", CardType.MONSTER, UUID.randomUUID(), 10, 10).toString()
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