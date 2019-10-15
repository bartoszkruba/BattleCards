package models

import java.util.*

abstract class User(var name: String) {
    val id:UUID = UUID.randomUUID()

    init {
        require(name.length < 11){"Name is too long, length is restricted to 1-10 characters"}
        require(name.isNotEmpty()){"Name is too short, length is restricted to 1-10 characters"}
        require(!name.contains("[^A-Za-z0-9]+".toRegex()))
    }
}