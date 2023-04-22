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



}