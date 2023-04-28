package models

import utils.Utilities
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
data class Swimmer(
    var swimmerId: Int = 0,
    var swimmerName: String,
    var swimmerLevel: Int,
    var swimmerCategory: String,
    var isSwimmerArchived: Boolean = false,
    var races: MutableSet<Race> = mutableSetOf(),
    var creationTime: LocalDateTime = LocalDateTime.now(),
    var updateTime: LocalDateTime = LocalDateTime.now()
) {
    private var lastRaceId = 0
    private fun getRaceId() = lastRaceId++

    /**
     * Adds a new race to the swimmer's list of races, assigning it a unique ID.
     * @param race The race to be added.
     * @return true if the race was successfully added, false otherwise.
     */
    fun addRace(race: Race): Boolean {
        race.raceId = getRaceId()
        return races.add(race)
    }

    /**
     * Returns the number of races associated with the swimmer.
     * @return The number of races.
     */
    fun numberOfRaces() = races.size

    /**
     * Finds a race in the swimmer's list of races by its unique ID.
     * @param id The unique ID of the race to find.
     * @return The found Race object, or null if not found.
     */
    fun findOne(id: Int): Race? {
        return races.find { race -> race.raceId == id }
    }

    /**
     * Deletes a race from the swimmer's list of races by its unique ID.
     * @param id The unique ID of the race to delete.
     * @return true if the race was successfully deleted, false otherwise.
     */
    fun delete(id: Int): Boolean {
        return races.removeIf { race -> race.raceId == id }
    }

    /**
     * Updates the details of a race in the swimmer's list of races.
     * @param id The unique ID of the race to update.
     * @param newRace The updated Race object containing new race information.
     * @return true if the race was successfully updated, false otherwise.
     */
    fun update(id: Int, newRace: Race): Boolean {
        val foundRace = findOne(id)

        if (foundRace != null) {
            foundRace.raceGraded = newRace.raceGraded
            foundRace.isRaceOutdated = newRace.isRaceOutdated
            return true
        }
        return false
    }

    /**
     * Checks if all races associated with the swimmer are completed (outdated).
     * @return true if all races are completed, false otherwise.
     */
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

    /**
     * Returns a formatted string containing a list of the swimmer's races.
     * @return A string representing the swimmer's races, or "NO RACES ADDED" if the swimmer has no races.
     */
    fun listRaces() =
        if (races.isEmpty()) "\tNO RACES ADDED"
        else Utilities.formatSetString(races)

    /**
     * Returns a string representation of the swimmer object, including their ID, name, level, category, archived status, info about time and list of races.
     * @return A formatted string representing the swimmer.
     */
    override fun toString(): String {
        val formatTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val archived = if (isSwimmerArchived) 'Y' else 'N'
        val currentDate = LocalDateTime.now().format(formatTime)
        val blue = "\u001B[34m"
        val magenta = "\u001B[35m"

        return """
    $blue+------------------------------+
     ID: $magenta$swimmerId$blue
     Name: $magenta$swimmerName$blue
     Level: $magenta$swimmerLevel$blue
     Category: $magenta$swimmerCategory$blue

     Archived: $magenta$archived$blue
     Creation Time: $magenta${creationTime.format(formatTime)}$blue
     Last Updated: $magenta${updateTime.format(formatTime)}$blue
     Current Date: $magenta$currentDate$blue

     Number of Races: $magenta${races.size}$blue
     Races: $magenta${listRaces()}$blue
    $blue+------------------------------+ 
        """.trimIndent()
    }
}
