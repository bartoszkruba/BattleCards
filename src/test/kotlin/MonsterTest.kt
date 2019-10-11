import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MonsterTest {

    @Test
    fun ConstructorTest() {
        constructorWithNoArgument();
        constructorWithArgument();
    }

    fun constructorWithNoArgument(){
        var monster = Monster()
        var monster2 = Monster()
        assertEquals(monster.health, monster2.health)
        assertNotNull(monster)
    }

    fun constructorWithArgument(){
        var monster1 = Monster("", CardType.MONSTER, 235,6,4);
        var monster = Monster("", CardType.MONSTER, 235,6,4);
        assertNotSame(monster, monster1)
        assertEquals("", monster.name)
        assertEquals(CardType.MONSTER, monster.type)
        assertEquals(235, monster.cardId)
        assertEquals(6, monster.attack)
        assertEquals(4, monster.health)
    }

    @Test
    fun takeDamge() {
        var monster1 = Monster("", CardType.MONSTER,235,6, 3);
        var monster2 = Monster("", CardType.MONSTER,235,7,4);

        var a = monster2.takeDamge(monster1)
        assertTrue(a)

        var s = monster2.takeDamge(monster1)
        assertFalse(s)
    }

    @Test
    fun isDead() {
        var monster1 = Monster("", CardType.MONSTER,235,4, 3);
        var monster2 = Monster("", CardType.MONSTER,235,7,10);

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamge(monster1)
        assertTrue(monster2.isDead())
    }
}