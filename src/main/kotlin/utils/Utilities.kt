package utils

import models.Race
import models.Swimmer

/**
 * Utilities is a singleton object that provides utility methods for formatting lists and sets of Swimmer and Race objects.
 */
object Utilities {

    /**
     * Formats a list of Swimmer objects into a readable string.
     * @param notesToFormat The list of Swimmer objects to format.
     * @return A formatted string representing the list of Swimmer objects.
     */
    @JvmStatic
    fun formatListString(notesToFormat: List<Swimmer>): String =
        notesToFormat
            .joinToString(separator = "\n") { swimmer ->  "$swimmer" }

    /**
     * Formats a set of Race objects into a readable string.
     * @param itemsToFormat The set of Race objects to format.
     * @return A formatted string representing the set of Race objects.
     */
    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Race>): String =
        itemsToFormat
            .joinToString(separator = "\n") { race ->  "\t$race" }

}