package prototype

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class InitCardsTest {

    lateinit var cardLoader: CardLoader
    lateinit var initCards: InitCards

    companion object {
        const val MAX_NAME_LENGTH = Settings.MAX_CARD_NAME_LENGTH
        const val MAX_HEALTH = Settings.MAX_HEALTH
        const val MAX_DAMAGE = Settings.MAX_DAMAGE
    }

    val NAME: String

    init {
        val sb = StringBuilder()
        repeat(MAX_NAME_LENGTH) { sb.append("a") }
        NAME = sb.toString()
    }

    @BeforeEach
    internal fun setUp() {
        cardLoader = mock(CardLoader::class.java)
        initCards = InitCards(cardLoader)
    }

    @Test
    internal fun `Test init script`() {
        val monsterOne = MonsterPrototype(1, NAME, MAX_HEALTH, MAX_DAMAGE)

        given(cardLoader.loadCards()).willReturn(arrayListOf(monsterOne))

        initCards.initCards()

        verify(cardLoader, times(1)).loadCards()
        verify(cardLoader, times(1)).deleteCards(arrayListOf(1))
        verify(cardLoader, times(1)).saveCards(any())
        verify(cardLoader, times(1)).saveDeck(any())
        verify(cardLoader, times(1)).loadDeck(any())

        verifyNoMoreInteractions(cardLoader)
    }

    fun <T> any(): T = Mockito.any<T>()
}