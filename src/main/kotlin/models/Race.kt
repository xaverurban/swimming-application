package models

data class Race (var raceId: Int = 0,
                 var raceGraded : String,
                 var isRaceOutdated: Boolean = false){

    override fun toString(): String {

        return if (isRaceOutdated)
            "$raceId: $raceGraded (Complete)"
        else
            "$raceId: $raceGraded (TODO)"
    }

}