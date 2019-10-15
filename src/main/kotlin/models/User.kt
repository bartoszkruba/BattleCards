package models

import java.util.*

abstract class User(var name: String) {
    val id:UUID = UUID.randomUUID()
}