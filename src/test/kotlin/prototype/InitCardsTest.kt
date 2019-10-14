package prototype

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class InitCardsTest {

    lateinit var cardLoader: CardLoader
    lateinit var initCards: InitCards

    @BeforeEach
    internal fun setUp() {
        cardLoader = mock(CardLoader::class.java)
        initCards = InitCards(cardLoader)
    }

    @Test
    internal fun `Test init script`() {
        val monsterOne = MonsterPrototype(1, "name", 5, 5)

        given(cardLoader.loadCards()).willReturn(arrayListOf(monsterOne))

        initCards.initCards()

        verify(cardLoader, times(1)).loadCards()
        verify(cardLoader, times(1)).deleteCards(arrayListOf(1))
        verify(cardLoader, times(1)).saveCards(any())
        verifyNoMoreInteractions(cardLoader)
    }

    fun <T> any(): T = Mockito.any<T>()
}