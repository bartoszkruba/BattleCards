package utilities

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

class Utils {
    companion object {
        private val mapper: ObjectMapper = ObjectMapper()

        fun clone(obj: Any): Any {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val clone = mapper.writeValueAsString(obj)
            val type = mapper.typeFactory.constructType(obj::class.java)
            return mapper.readValue(clone, type)
        }
    }
}