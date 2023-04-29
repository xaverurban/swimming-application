package utils

object CategoryUtility {

    @JvmStatic
    val categories = setOf("Freestyle", "Backstroke", "Breaststroke", "Butterfly", "Medley")

    @JvmStatic
    fun isValidCategory(categoryToCheck: String?): Boolean {
        for (category in categories) {
            if (category.equals(categoryToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
