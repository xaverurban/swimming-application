
package controllers

import models.Swimmer
import persistence.Serializer
/**
 *This class provides an API for managing a list of swimmers and their associated races. It offers functionality to
 *add, search, list, update, and delete swimmers, as well as manage their races.
 *The SwimmerAPI interacts with a provided Serializer object to support storing and loading swimmer data.
 *It allows users to perform various operations on swimmers such as searching by name, listing active or archived
 *swimmers, and updating or archiving swimmers.
 *Additionally, this class provides methods to search for races based on their contents and list upcoming races,
 *as well as count swimmers and races based on different criteria.
 *@author Xaver Urban
 *@since v1.0
 * */

/**
 * SwimmerAPI class manages the swimmer data and provides various methods to manipulate
 * and query swimmer information.
 *
 * @param serializerType The type of serializer to be used for storing and loading data.
 */
class SwimmerAPI(serializerType: Serializer) {

    private var swimmers = ArrayList<Swimmer>()
    private var serializer: Serializer = serializerType

    private var lastId = 0
    private fun getId() = lastId++

    /**
     * Adds a new swimmer to the list of swimmers and assigns a unique ID to the swimmer.
     *
     * @param swimmer The swimmer object to be added.
     * @return Boolean indicating whether the swimmer was successfully added to the list.
     */
    fun add(swimmer: Swimmer): Boolean {
        swimmer.swimmerId = getId()
        return swimmers.add(swimmer)
    }

    /**
     * Searches for swimmers in the list by their name and returns a formatted string with the results.
     *
     * @param searchString The search string to be matched against swimmer names.
     * @return A formatted string with the list of matching swimmers.
     */
    fun searchSwimmersByName(searchString: String) =
        formatListString(
            swimmers.filter { swimmer -> swimmer.swimmerName.contains(searchString, ignoreCase = true) }
        )

    /**
     * Searches for races in the list of swimmers by their contents and returns a formatted string with the results.
     * This method searches through all the races of each swimmer and returns those races that contain the given
     * search string.
     *
     * @param searchString The search string to be matched against race contents.
     * @return A formatted string with the list of matching races along with their respective swimmer details.
     */
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

    /**
     * Lists all ungraded races for all swimmers in a formatted string.
     *
     * @return A formatted string containing the list of upcoming races, or "No swimmer stored" if there are no swimmers.
     */
    fun listUngradedRaces(): String =
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

    /**
     * Counts the total number of ungraded races for all swimmers.
     *
     * @return The total number of upcoming races.
     */
    fun numberOfUngradedRaces(): Int {
        var numberOfUngradedRaces = 0
        for (Swimmer in swimmers) {
            for (race in Swimmer.races) {
                if (!race.isRaceOutdated) {
                    numberOfUngradedRaces++
                }
            }
        }
        return numberOfUngradedRaces
    }

    /**
     * Searches for swimmers in the list by their name and returns a list of matching swimmer objects.
     *
     * @param name The search string to be matched against swimmer names.
     * @return A list of Swimmer objects with matching names.
     */
    fun searchByName(name: String): List<Swimmer> = swimmers.filter { swimmer: Swimmer -> swimmer.swimmerName.contains(name, ignoreCase = true) }

    /**
     * Lists all swimmers in a formatted string.
     *
     * @return A formatted string containing the list of all swimmers, or "No swimmer stored" if there are no swimmers.
     */
    fun listAllSwimmers() =
        if (swimmers.isEmpty()) "No swimmer stored"
        else formatListString(swimmers)

    /**
     * Lists all active swimmers in a formatted string.
     *
     * @return A formatted string containing the list of active swimmers, or "No active swimmers stored" if there are none.
     */
    fun listActiveSwimmers() =
        if (numberOfActiveSwimmers() == 0) "No active swimmers stored"
        else formatListString(swimmers.filter { swimmer -> !swimmer.isSwimmerArchived })

    /**
     * Lists all archived swimmers in a formatted string.
     *
     * @return A formatted string containing the list of archived swimmers, or "No archived swimmers stored" if there are none.
     */
    fun listArchivedSwimmers(): String =
        if (numberOfArchivedSwimmers() == 0) "No archived swimmers stored"
        else formatListString(swimmers.filter { swimmer -> swimmer.isSwimmerArchived })

    /**
     * Counts the total number of swimmers at a specific level.
     *
     * @param level The level to match against swimmer levels.
     * @return The total number of swimmers at the specified level.
     */
    fun numberOfSwimmersByLevel(level: Int): Int = swimmers.count { swimmer: Swimmer -> swimmer.swimmerLevel == level }

    /**
     * Archives a swimmer if they exist, are not already archived, and have completed all their races.
     *
     * @param id The unique ID of the swimmer to be archived.
     * @return Boolean indicating whether the swimmer was successfully archived.
     */
    fun archiveSwimmer(id: Int): Boolean {
        val foundSwimmer = findSwimmer(id)
        if ((foundSwimmer != null) && (!foundSwimmer.isSwimmerArchived) &&
            (foundSwimmer.checkSwimmerCompletionStatus())
        ) {
            foundSwimmer.isSwimmerArchived = true
            return true
        }
        return false
    }

    /**
     * Finds a swimmer in the list by their unique ID.
     *
     * @param swimmerId The unique ID of the swimmer to be found.
     * @return The Swimmer object if found, or null if not found.
     */
    fun findSwimmer(swimmerId: Int) = swimmers.find { swimmer -> swimmer.swimmerId == swimmerId }

    /**
     * Counts the total number of swimmers in the list.
     *
     * @return The total number of swimmers.
     */
    fun numberOfSwimmers() = swimmers.size

    /**
     * Counts the total number of archived swimmers in the list.
     *
     * @return The total number of archived swimmers.
     */
    fun numberOfArchivedSwimmers(): Int = swimmers.count { swimmer: Swimmer -> swimmer.isSwimmerArchived }

    /**
     * Counts the total number of active swimmers in the list.
     *
     * @return The total number of active swimmers.
     */
    fun numberOfActiveSwimmers(): Int = swimmers.count { swimmer: Swimmer -> !swimmer.isSwimmerArchived }

    /**
     * Deletes a swimmer from the list by their unique ID.
     *
     * @param id The unique ID of the swimmer to be deleted.
     * @return Boolean indicating whether the swimmer was successfully deleted.
     */
    fun delete(id: Int) = swimmers.removeIf { swimmer -> swimmer.swimmerId == id }

    /**
     * Updates the details of a swimmer in the list.
     *
     * @param id The unique ID of the swimmer to be updated.
     * @param swimmer The Swimmer object containing the updated details.
     * @return Boolean indicating whether the swimmer was successfully updated.
     */
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

    /**
     * Loads the swimmers list from the serializer.
     *
     * @throws Exception if an error occurs during the read operation.
     */
    @Throws(Exception::class)
    fun load() {
        swimmers = serializer.read() as ArrayList<Swimmer>
    }

    /**
     * Stores the swimmers list using the serializer.
     *
     * @throws Exception if an error occurs during the write operation.
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(swimmers)
    }

    /**
     * Formats a list of Swimmer objects into a string representation.
     *
     * @param swimmersToFormat The list of Swimmer objects to be formatted.
     * @return A formatted string containing the list of swimmers.
     */
    private fun formatListString(swimmersToFormat: List<Swimmer>): String =
        swimmersToFormat
            .joinToString(separator = "\n") { swimmer ->
                swimmers.indexOf(swimmer).toString() + ": " + swimmer.toString()
            }
}
