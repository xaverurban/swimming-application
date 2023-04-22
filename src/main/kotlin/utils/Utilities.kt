package utils

import models.Race
import models.Swimmer

object Utilities {

    @JvmStatic
    fun formatListString(notesToFormat: List<Swimmer>): String =
        notesToFormat
            .joinToString(separator = "\n") { swimmer ->  "$swimmer" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Race>): String =
        itemsToFormat
            .joinToString(separator = "\n") { race ->  "\t$race" }

}