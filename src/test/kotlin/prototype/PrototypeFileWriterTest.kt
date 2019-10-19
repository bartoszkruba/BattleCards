package prototype

import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.nio.file.Path

internal class PrototypeFileWriterTest {

    companion object {
        private val testPath = Path.of("test", "test.json").toAbsolutePath().toString()

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            Path.of("test").toFile().mkdir()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            val file = File(testPath)
            if (file.exists()) file.delete()

            val fileOne = Path.of("test", NAME_ONE).toFile()
            val fileTwo = Path.of("test", NAME_TWO).toFile()
            val fileThree = Path.of("test", NAME_THREE).toFile()

            if (fileOne.exists()) fileOne.delete()
            if (fileTwo.exists()) fileTwo.delete()
            if (fileThree.exists()) fileThree.delete()
        }

        private const val TEST_STRING = "test"

        private const val NAME_ONE = "test1.json"
        private const val NAME_TWO = "test2.txt"
        private const val NAME_THREE = "lol"
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

    @Test
    internal fun `Read all files in directory`() {

        val fileOne = Path.of("test", NAME_ONE).toAbsolutePath().toString()
        val fileTwo = Path.of("test", NAME_TWO).toAbsolutePath().toString()
        val fileThree = Path.of("test", NAME_THREE).toAbsolutePath().toString()

        File(fileOne).writeText(TEST_STRING)
        File(fileTwo).writeText(TEST_STRING)
        File(fileThree).mkdir()

        val files = prototypeFileWriter.filesInDirectory(testPath)

        assertEquals(2, files.size)
        assertTrue(listOf(NAME_ONE, NAME_TWO).containsAll(files))
    }
}