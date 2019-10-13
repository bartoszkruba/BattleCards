package prototype

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonSubTypes.Type


//// todo add type
//@JsonTypeInfo(
//    visible = true,
//    use = JsonTypeInfo.Id.NAME,
//    include = JsonTypeInfo.As.PROPERTY,
//    property = "type"
//)
//@JsonSubTypes(
//    Type(value = MonsterPrototype::class, name = "monster")
//)
abstract class CardPrototype(open val id: Int, open val name: String) {

    companion object {
        // todo get information from settings
        val MAX_NAME_LENGTH = 9
        val MIN_NAME_LENGTH = 1
        val NAME_REGEX = Regex("[a-zA-z ]*")
    }
}