package controllers

import models.Swimmer
import persistence.Serializer
import java.util.ArrayList

class SwimmerAPI(serializerType: Serializer) {

    private var swimmers = ArrayList<Swimmer>()
    private var serializer: Serializer = serializerType

    private var lastId = 0
    private fun getId() = lastId++


    fun add(swimmer: Swimmer): Boolean {
        swimmer.swimmerId = getId()
        return swimmers.add(swimmer)
    }


    fun searchSwimmersByName(searchString: String) =
        formatListString(
            swimmers.filter { swimmer -> swimmer.swimmerName.contains(searchString, ignoreCase = true) }
        )


    fun searchRaceByContents(searchString: String): String {
        return if (numberOfSwimmers() == 0) "No swimmer stored"
        else {
            var listOfSwimmers = ""
            for (swimmer in swimmers) {
                for (race in swimmer.races) {
                    if (race.raceGraded.contains(searchString, ignoreCase = true)) {
                        listOfSwimmers += "${swimmer.swimmerId}: ${swimmer.swimmerName} \n\t${race}\n"
                    }
                }
            }
            if (listOfSwimmers == "") "No races found for: $searchString"
            else listOfSwimmers
        }
    }


    fun listTodoRaces(): String =
        if (numberOfSwimmers() == 0) "No swimmer stored"
        else {
            var listOfUngradedRaces = ""
            for (swimmer in swimmers) {
                for (race in swimmer.races) {
                    if (!race.isRaceOutdated) {
                        listOfUngradedRaces += swimmer.swimmerName + ": " + race.raceGraded + "\n"
                    }
                }
            }
            listOfUngradedRaces
        }

    fun numberOfToDoRaces(): Int {
        var numberOfToDoRaces = 0
        for (Swimmer in swimmers) {
            for (race in Swimmer.races) {
                if (!race.isRaceOutdated) {
                    numberOfToDoRaces++
                }
            }
        }
        return numberOfToDoRaces
    }

    fun searchByName(name: String): List<Swimmer> = swimmers.filter { swimmer: Swimmer -> swimmer.swimmerName.contains(name, ignoreCase = true) }

    fun listAllSwimmers() =
        if (swimmers.isEmpty()) "No swimmer stored"
        else formatListString(swimmers)

    fun listActiveSwimmers() =
        if (numberOfActiveSwimmers() == 0) "No active swimmers stored"
        else formatListString(swimmers.filter { swimmer -> !swimmer.isSwimmerArchived })

    fun listArchivedSwimmers(): String =
        if (numberOfArchivedSwimmers() == 0) "No archived swimmers stored"
        else formatListString(swimmers.filter { swimmer -> swimmer.isSwimmerArchived })
    fun numberOfSwimmersByLevel(level: Int): Int = swimmers.count { swimmer: Swimmer -> swimmer.swimmerLevel == level }




    fun archiveSwimmer(id: Int): Boolean {
        val foundSwimmer = findSwimmer(id)
        if ((foundSwimmer != null) && (!foundSwimmer.isSwimmerArchived)
            && (foundSwimmer.checkSwimmerCompletionStatus())
        ) {
            foundSwimmer.isSwimmerArchived = true
            return true
        }
        return false
    }


    fun findSwimmer(swimmerId : Int) =  swimmers.find{ swimmer -> swimmer.swimmerId == swimmerId }

    fun numberOfSwimmers() = swimmers.size
    fun numberOfArchivedSwimmers(): Int = swimmers.count { swimmer: Swimmer -> swimmer.isSwimmerArchived }
    fun numberOfActiveSwimmers(): Int = swimmers.count { swimmer: Swimmer -> !swimmer.isSwimmerArchived }
    fun delete(id: Int) = swimmers.removeIf { swimmer -> swimmer.swimmerId == id }

    fun update(id: Int, swimmer: Swimmer?): Boolean {
        // find the Swimmer object by the index number
        val foundSwimmer = findSwimmer(id)
        // if the Swimmer exists, use the Swimmer details passed as parameters to update the found Swimmer in the ArrayList.
        if ((foundSwimmer != null) && (swimmer != null)) {
            foundSwimmer.swimmerName = swimmer.swimmerName
            foundSwimmer.swimmerLevel = swimmer.swimmerLevel
            foundSwimmer.swimmerCategory = swimmer.swimmerCategory
            return true
        }
        // if the Swimmer was not found, return false, indicating that the update was not successful
        return false
    }
    @Throws(Exception::class)
    fun load() {
        swimmers = serializer.read() as ArrayList<Swimmer>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(swimmers)
    }
    private fun formatListString(swimmersToFormat : List<Swimmer>) : String =
        swimmersToFormat
            .joinToString (separator = "\n") { swimmer ->
                swimmers.indexOf(swimmer).toString() + ": " + swimmer.toString() }

}

