package models

data class Races (var swimmerId: Int = 0,
                  var raceGraded : String,
                  var isRaceOutdated: Boolean = false){

    override fun toString(): String {

        return if (isRaceOutdated)
            "$swimmerId: $raceGraded (Complete)"
        else
            "$swimmerId: $raceGraded (TODO)"
    }

}