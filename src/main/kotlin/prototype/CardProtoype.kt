package prototype

import java.util.prefs.Preferences.MAX_NAME_LENGTH


// todo add type
abstract class CardPrototype(open val id: Int, open val name: String) {

    companion object {
        // todo get information from settings
        val MAX_NAME_LENGTH = 9
        val MIN_NAME_LENGTH = 1
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }
}