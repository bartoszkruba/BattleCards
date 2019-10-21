package models

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import Monster

internal class FieldTest {

    @Test
    fun wakeUpMonsters() {
        var field:Field = Field(arrayListOf(Monster("Hejsan",2,5),Monster("Svejsan",2,5),Monster("Haj",2,5)))
        field.cardsInList().forEach { if (it is Monster){
            it.sleeping = true}
        }
        field.wakeUpMonsters()
        checkSleeping(field,false)
    }

    private fun checkSleeping(field: Field, shouldSleep:Boolean){
        var errorMsg = if(shouldSleep) "Monster should be sleeping" else "Monster should not be sleeping"
        field.cardsInList().forEach{
            if (it is Monster){
                assertEquals(shouldSleep, it.sleeping,errorMsg)
            }
        }
    }

    @Test
    fun cardsAreSleepingTest(){
        var field:Field = Field(arrayListOf(Monster("Hejsan",2,5),Monster("Svejsan",2,5),Monster("Haj",2,5)))
        field.cardsInList().forEach { if (it is Monster){
            it.sleeping = true}
        }
        assertTrue(field.allCardsAreSleeping())
        field.wakeUpMonsters()
        assertFalse(field.allCardsAreSleeping())
        var monster = field.cardsInList()[0] as Monster
        monster.sleeping = true
        assertTrue(field.allCardsAreSleeping())
    }
}