package models

import utils.Utilities

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



    fun listRaces() =
        if (races.isEmpty())  "\tNO RACES ADDED"
        else  Utilities.formatSetString(races)
    override fun toString(): String {
        val archived = if (isSwimmerArchived) 'Y' else 'N'
        return "$swimmerId: $swimmerName, Priority($swimmerLevel), Category($swimmerCategory), Archived($archived) \n${listRaces()}"
    }

}