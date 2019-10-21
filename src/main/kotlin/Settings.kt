abstract class Settings {
    companion object {
        const val MENU_OPTION_ATTACK_MONSTER = "Attack Monster"
        const val MENU_OPTION_DRAW_CARD = "Draw Card"
        const val MENU_OPTION_PLACE_CARD = "Place Card"
        const val MENU_OPTION_END_ROUND = "End Round"

        const val DECK_SIZE: Int = 30
        const val FIELD_SIZE: Int = 5
        const val HAND_SIZE: Int = 5
        const val MIN_DAMAGE: Int = 1
        const val MAX_DAMAGE: Int = 10
        const val MIN_HEALTH: Int = 1
        const val MAX_HEALTH: Int = 10
        const val PLAYER_MANA: Int = 2
        const val MAX_CARD_NAME_LENGTH = 9
        const val MIN_CARD_NAME_LENGTH = 1
        const val MAX_DECK_NAME_LENGTH = 20
        const val MIN_DECK_NAME_LENGTH = 1

        const val ANSI_RESET = "\u001B[0m"
        const val ANSI_BLACK = "\u001B[30m"
        const val ANSI_RED = "\u001B[31m"
        const val ANSI_GREEN = "\u001B[32m"
        const val ANSI_YELLOW = "\u001B[33m"
        const val ANSI_BLUE = "\u001B[34m"
        const val ANSI_PURPLE = "\u001B[35m"
        const val ANSI_CYAN = "\u001B[36m"
        const val ANSI_WHITE = "\u001B[37m"
    }
}