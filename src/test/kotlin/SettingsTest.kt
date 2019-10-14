import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.memberProperties

internal class SettingsTest {

    @Test
    internal fun settingsConstructorTest() {
        assertTrue(Settings::class.isAbstract, "Can't create instance from abstract class")

        assertEquals(0, Settings::class.memberProperties.toList().size)

        Settings::class.companionObject!!.memberProperties.forEach {
           assertTrue(it.isConst, "Property $it must be a constant")
        }
    }
}