package prototype

import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Path

internal class PrototypeFileWriterTest {

    companion object {
        private val testPath = Path.of("test", "test.json").toAbsolutePath().toString()

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            val file = File(testPath)
            if (file.exists()) file.delete()
        }

        private const val TEST_STRING = "test"
    }

    lateinit var prototypeFileWriter: PrototypeFileWriter

    @BeforeEach
    internal fun setUp() {
        prototypeFileWriter = PrototypeFileWriter()
    }

    @AfterEach
    internal fun tearDown() {
        val file = File(testPath)
        if (file.exists()) file.delete()
    }

    @Test
    fun `Writing file goes right`() {
        prototypeFileWriter.writeFile(testPath, TEST_STRING)

        val file = File(testPath)
        assertEquals(TEST_STRING, file.readText())
    }

    @Test
    internal fun `Reading file goes right`() {
        val file = File(testPath)
        file.writeText(TEST_STRING)

        assertEquals(TEST_STRING, prototypeFileWriter.readFile(testPath))
    }

    @Test
    internal fun `Reading non existing file`() {
        assertFalse(File(testPath).exists())

        assertNull(prototypeFileWriter.readFile(testPath))
    }
}