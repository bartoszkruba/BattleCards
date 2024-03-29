import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class MonsterTest {

    @Test
    fun constructorTest() {
        constructorWithNoArgument()
        constructorWithArgument()
    }

    private fun constructorWithNoArgument() {
        val monster = Monster()
        val monster2 = Monster()
        assertNotEquals(monster.cardId, monster2.cardId)
        assertNotNull(monster)
        assertNotNull(monster2)
        assertNotSame(monster2, monster)
    }

    private fun constructorWithArgument(){
        val monster = Monster("Ogre tok",6,4)
        val monster2 = Monster("Ogre",6,4)
        assertNotSame(monster2, monster)
        assertNotEquals(monster2.cardId, monster.cardId)
        assertEquals("Ogre", monster2.name)
        assertEquals(CardType.MONSTER, monster2.type)
        assertEquals(6, monster2.attack)
        assertEquals(4, monster2.health)
    }

    @Test
    fun takeDamage() {
        val monster1 = Monster("Ogre", 6, 3)
        val monster2 = Monster("Ogre", 7,4)

        val a = monster2.takeDamage(monster1)
        assertTrue(a)

        val s = monster2.takeDamage(monster1)
        assertFalse(s)
    }

    @Test
    fun isDead() {
        val monster1 = Monster("Ogre",4, 3)
        val monster2 = Monster("Ogre",7,9)

        monster2.takeDamage(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamage(monster1)
        assertFalse(monster2.isDead())

        monster2.takeDamage(monster1)
        assertTrue(monster2.isDead())
    }

    @Test
    fun toStringTest() {
        val sleepTestArray: Array<Boolean> = arrayOf(true, false)

        repeat(2) {
            val sleepTest = sleepTestArray[it]
            val sleepStart = if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RESET
            val sleepEnd = Settings.ANSI_RESET

            val wolfCard = Monster("Wolf", 3, 6)
            wolfCard.sleeping = sleepTest
            var atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}3 ${if(sleepTest) "" else Settings.ANSI_RESET}"
            var hp =  "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED} 6${Settings.ANSI_RESET}"
            var name ="${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}  Wolf     ${Settings.ANSI_RESET}"

            val wolfCardLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                         "$atk|___|$hp  ",
                              name
            )
            val wolfCardTest = wolfCardLines.joinToString("\n")

            assertEquals(wolfCardTest, wolfCard.toString(), "The toString doesn't match")

            val gnarlCard = Monster("Gnarl", 8, 5)
            gnarlCard.sleeping = sleepTest
            atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}8 ${if(sleepTest) "" else Settings.ANSI_RESET}"
            hp =  "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED} 5${Settings.ANSI_RESET}"
            name ="${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}  Gnarl    ${Settings.ANSI_RESET}"

            val gnarlCardLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                         "$atk|___|$hp  ",
                              name
            )
            val gnarlCardTest = gnarlCardLines.joinToString("\n")

            assertEquals(gnarlCardTest, gnarlCard.toString(), "The toString doesn't match")

            val skeletonCard = Monster("Skeleton",10, 10)
            skeletonCard.sleeping = sleepTest
            atk = "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_BLUE}10${if(sleepTest) "" else Settings.ANSI_RESET}"
            hp =  "${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_RED}10${Settings.ANSI_RESET}"
            name ="${if(sleepTest) Settings.ANSI_WHITE else Settings.ANSI_GREEN}Skeleton   ${Settings.ANSI_RESET}"

            val skeletonCardLines: Array<String> = arrayOf(
                "$sleepStart   ___     $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                "$sleepStart  |   |    $sleepEnd",
                         "$atk|___|$hp  ",
                              name
            )
            val skeletonCardTest = skeletonCardLines.joinToString("\n")

            assertEquals(skeletonCardTest, skeletonCard.toString(), "The toString doesn't match")
        }
    }
}