package inAndOutputAdapter

class Input() {
    fun userNameInput(name: String?): Boolean {
        return userNameValidation(name)
    }

    fun deckNameInput(deckName: String?): Boolean {
        return checkDeckName(deckName)
    }

    fun userNameValidation(name: String?):Boolean{
        val regex = Regex("^[a-zA-Z]{1,9}")
        return if (name != null) {
            name.length in 1..9 && regex.matches(name)
        }else{
            false
        }
    }

    fun checkDeckName(name: String?):Boolean{
        val decks = listOf("one", "two", "three", "four")
        for (iteam in decks){
            if(iteam == name){
                return true
            }
        }
        return false
    }
}