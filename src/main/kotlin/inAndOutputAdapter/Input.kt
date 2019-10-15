package inAndOutputAdapter

class Input() {
    fun userNameInput(name: String?): Boolean {
        return userNameValidation(name)
    }

    fun deckNameInput(deckName: String?): Unit? {
        return null
    }

    fun userNameValidation(name: String?):Boolean{
        val regex = Regex("^[a-zA-Z]{1,9}")
        return if (name != null) {
            name.length in 1..9 && regex.matches(name)
        }else{
            false
        }
    }
}