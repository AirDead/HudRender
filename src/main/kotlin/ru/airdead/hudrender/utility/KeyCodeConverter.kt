package ru.airdead.hudrender.utility

object KeyCodeConverter {

    private val charToKeyCodeMap = mapOf(
        '0' to 48, '1' to 49, '2' to 50, '3' to 51, '4' to 52,
        '5' to 53, '6' to 54, '7' to 55, '8' to 56, '9' to 57,
        ' ' to 32, ',' to 44, '.' to 46, ';' to 59, '=' to 61
    ) + ('A'..'Z').associateWith { it.code } +
            ('a'..'z').associateWith { it.uppercaseChar().code }

    private val keyCodeToCharMap = charToKeyCodeMap.entries.associate { it.value to it.key }

    /**
     * Converts a key code to its corresponding character, taking into account the active modifiers.
     *
     * @param keyCode The key code to convert.
     * @param modifiers The set of active modifiers.
     * @return The corresponding character, or null if the key code does not map to a character.
     */
    fun keyCodeToChar(keyCode: Int, modifiers: Set<Modifiers>): Char? {
        return keyCodeToCharMap[keyCode]?.let {
            if (it.isLetter() && modifiers.contains(Modifiers.SHIFT)) it.uppercaseChar() else it
        }
    }

    /**
     * Converts a character to its corresponding key code.
     *
     * @param char The character to convert.
     * @return The corresponding key code, or null if the character does not map to a key code.
     */
    fun charToKeyCode(char: Char): Int? {
        return charToKeyCodeMap[char]
    }
}
