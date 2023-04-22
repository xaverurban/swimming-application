package models

data class Swimmer(var swimmerId: Int = 0,
                var swimmerName: String,
                var swimmerLevel: Int,
                var swimmerCategory: String,
                var isSwimmerArchived: Boolean = false,
                var races : MutableSet<Races> = mutableSetOf()) {
}
