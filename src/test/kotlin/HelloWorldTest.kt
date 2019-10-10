import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class HelloWorldTest {

    @Test
    fun helloWorld() {
        val helloWorld = HelloWorld()

        assertEquals(helloWorld.helloWorld(), "Hello Word", "Hello World")
    }
}