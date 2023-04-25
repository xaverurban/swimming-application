package models

import utils.Utilities

/**
*
 *Represents a swimmer with their associated details and races. The Swimmer class provides a convenient
 *way to organize and manage swimmer information, including their name, level, category, and
 *associated races. This class also provides methods for managing a swimmer's races and checking
 *their completion status. The Swimmer class is used in conjunction with the SwimmerAPI class for
 *managing a list of swimmers and their associated races.
 *@author Xaver Urban
 *@since v1.0
 */

/**
*
*Represents a swimmer with their associated details and races.
*@property swimmerId The unique ID of the swimmer.
*@property swimmerName The name of the swimmer.
*@property swimmerLevel The level of the swimmer (e.g., 1, 2, or 3).
*@property swimmerCategory The category of the swimmer (e.g., "Masters", "Youth").
*@property isSwimmerArchived Indicates whether the swimmer is archived or not. Defaults to false.
*@property races A mutable set of Race objects associated with the swimmer.
 */
data class Swimmer(var swimmerId: Int = 0,
                var swimmerName: String,
                var swimmerLevel: Int,
                var swimmerCategory: String,
                var isSwimmerArchived: Boolean = false,
                var races : MutableSet<Race> = mutableSetOf())

{
    private var lastRaceId = 0
    private fun getRaceId() = lastRaceId++

    fun addRace(race: Race) : Boolean {
        race.raceId = getRaceId()
        return races.add(race)
    }

    fun numberOfRaces() = races.size

    fun findOne(id: Int): Race?{
        return races.find{ race -> race.raceId == id }
    }

    fun delete(id: Int): Boolean {
        return races.removeIf { race -> race.raceId == id}
    }

    fun update(id: Int, newRace : Race): Boolean {
        val foundRace = findOne(id)


        if (foundRace != null){
            foundRace.raceGraded = newRace.raceGraded
            foundRace.isRaceOutdated = newRace.isRaceOutdated
            return true
        }
        return false
    }

    fun checkSwimmerCompletionStatus(): Boolean {
        if (races.isNotEmpty()) {
            for (race in races) {
                if (!race.isRaceOutdated) {
                    return false
                }
            }
        }
        return true
    }


    fun listRaces() =
        if (races.isEmpty())  "\tNO RACES ADDED"
        else  Utilities.formatSetString(races)
    override fun toString(): String {
        val archived = if (isSwimmerArchived) 'Y' else 'N'
        return "$swimmerId: $swimmerName, Priority($swimmerLevel), Category($swimmerCategory), Archived($archived) \n${listRaces()}"
    }

}