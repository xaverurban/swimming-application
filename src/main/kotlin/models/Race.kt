package models
/**
 * Represents a race, containing a unique ID, race grade, and graded status.
 * The Race class is used to store and manage information about individual races,
 * including their grade and whether they have been graded or not.
 *
 * @property raceId The unique ID of the race.
 * @property raceGraded The grade of the race.
 * @property isRaceOutdated Indicates whether the race is graded or not. Defaults to false.
 *
 * @author Xaver Urban
 * @since v1.0
 */

/**
*
*Represents a race, containing a unique ID, race grade, and graded status.
*
*@property raceId The unique ID of the race.
*
*@property raceGraded The grade of the race
*
*@property isRaceOutdated Indicates whether the race is graded or not. Defaults to false.
 */
data class Race (var raceId: Int = 0,
                 var raceGraded : String,
                 var isRaceOutdated: Boolean = false){

/**
*
*Returns a string representation of the race, including its ID, grade, and completion status.
*@return A formatted string representing the race.
*/
    override fun toString(): String {

        return if (isRaceOutdated)
            "$raceId: $raceGraded (Graded)"
        else
            "$raceId: $raceGraded  (Ungraded)"
    }

}