import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SettingsTest {

    @Test
    internal fun settingsConstructorTest() {
        assertTrue(Settings::class.isAbstract, "Can't create instance from abstract class")
    }
}