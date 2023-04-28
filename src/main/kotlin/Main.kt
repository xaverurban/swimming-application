

import controllers.SwimmerAPI
import models.Race
import models.Swimmer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import java.io.File
import kotlin.system.exitProcess

/**
 * This class provides an interactive menu-driven application for managing a list of swimmers and their associated races.
 * It offers functionality to add, search, list, update, and delete swimmers, as well as manage their races.
 * The SwimmerApp interacts with the SwimmerAPI and uses a Serializer object to support storing and loading swimmer data.
 * It allows users to perform various operations on swimmers such as adding, listing, updating, deleting, and archiving swimmers.
 * Additionally, this class provides functionality to add, update, and delete races associated with swimmers.
 * Users can also search for swimmers by name and races by their description.
 *
 * @author Xaver Urban
 * @since v1.0
 */

private val swimmerAPI = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
fun main() {
    runMenu()
}

/**
 * Displays the main menu of the Swimming App and returns the user's selected option as an integer.
 * The menu provides options for managing swimmers and races, including adding, listing, updating, deleting,
 * and archiving swimmers, as well as adding, updating, and deleting races associated with swimmers.
 * Additionally, the menu offers options to search for swimmers by name, search for races by description, and list graded races.
 *
 * @return An integer representing the user's selected menu option.
 */
fun mainMenu(): Int {
    val reset = "\u001B[0m"
    val title = "\u001B[36m"
    val sectionTitle = "\u001B[33m"
    val optionNumber = "\u001B[32m"
    val optionName = "\u001B[34m"

    return readNextInt(
        """
        $title+-----------------------------------------------------+
        |                  ${title}SWIMMING APP$reset                     
        +-----------------------------------------------------+
        | ${sectionTitle}NOTE MENU$reset                                         
        |   ${optionNumber}1) ${optionName}Add a Swimmer$reset                                
        |   ${optionNumber}2) ${optionName}List Swimmers$reset                                
        |   ${optionNumber}3) ${optionName}Update a Swimmer$reset                             
        |   ${optionNumber}4) ${optionName}Delete a Swimmer$reset                             
        |   ${optionNumber}5) ${optionName}Archive a Swimmer$reset                            
        +-----------------------------------------------------+
        | ${sectionTitle}SWIMMER MENU$reset                                      
        |   ${optionNumber}6) ${optionName}Add race to a Swimmer$reset                       
        |   ${optionNumber}7) ${optionName}Update race contents on a Swimmer$reset           
        |   ${optionNumber}8) ${optionName}Delete race from a Swimmer$reset                  
        |   ${optionNumber}9) ${optionName}Mark item as$reset                                
        +-----------------------------------------------------+
        | ${sectionTitle}REPORT MENU FOR SWIMMERS$reset                          
        |   ${optionNumber}10) ${optionName}Search for all Swimmers (by name)$reset           
        +-----------------------------------------------------+
        | ${sectionTitle}REPORT MENU FOR RACES$reset                             
        |    ${optionNumber}13) ${optionName}Add a race to the database$reset                 
        |    ${optionNumber}14) ${optionName}View all races$reset                             
        |    ${optionNumber}15) ${optionName}Search for all races (by race name)$reset        
        |    ${optionNumber}16) ${optionName}List graded races$reset                          
        +-----------------------------------------------------+
        |    ${optionNumber}0) ${optionName}Exit$reset                                         
        +-----------------------------------------------------+
        ==>> 
        """.trimIndent()
    )
}

/**
 * Executes the main loop of the Swimming App, displaying the main menu and executing the appropriate functions
 * based on the user's input. This loop continues until the user selects the exit option.
 */
fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addSwimmer()
            2 -> listSwimmers()
            3 -> updateSwimmer()
            4 -> deleteSwimmer()
            5 -> archiveSwimmer()
            6 -> addRaceToSwimmer()
            7 -> updateRaceGradedInSwimmer()
            8 -> deleteRace()
            9 -> markRaceStatus()
            //     10 -> searchSwimmers()
            //     15 -> searchRaces()
            //  16 -> list
            17 -> save()
            18 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

/**
 * Prompts the user for swimmer details (name, level, and category), creates a new Swimmer object with the provided
 * information, and attempts to add the Swimmer to the swimmerAPI. Displays a success or failure message based on
 * whether the Swimmer was added successfully.
 */
fun addSwimmer() {
    val swimmerName = ScannerInput.readNextLine("Enter a name for the swimmer: ")
    val swimmerLevel = readNextInt("Enter a level of swimmer (1-low, 2, 3, 4, 5-high): ")
    val swimmerCategory = ScannerInput.readNextLine("Enter a category for the swimmer: ")
    val isAdded = swimmerAPI.add(Swimmer(swimmerName = swimmerName, swimmerLevel = swimmerLevel, swimmerCategory = swimmerCategory))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

/**
 * Lists all swimmers and prompts the user to enter the ID of the swimmer to delete.
 * If a valid ID is entered, attempts to delete the swimmer from the swimmerAPI and displays a success or failure message.
 */
fun deleteSwimmer() {
    listSwimmers()
    if (swimmerAPI.numberOfSwimmers() > 0) {
        // only ask the user to choose the swimmer to delete if swimmers exist
        val id = readNextInt("Enter the id of the swimmer to delete: ")
        // pass the index of the swimmer to SwimmerAPI for deleting and check for success.
        val swimmerToDelete = swimmerAPI.delete(id)
        if (swimmerToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

/**
 * Prompts the user to select an active swimmer and, if a valid swimmer is chosen, adds a race to the swimmer.
 * Displays a success or failure message based on whether the race was added successfully.
 */
private fun addRaceToSwimmer() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        print("\nEnter race details:\n")
        val raceGraded = ScannerInput.readNextLine("\tRace Grade(eg. Pass, Fail): ")
        val raceTime = ScannerInput.readNextLine("\tRace Time(use format HH:mm:ss): ")
        val raceType = ScannerInput.readNextLine("\tRace Type(eg. Backstroke, Freestyle): ")
        if (swimmer.addRace(Race(raceGraded = raceGraded, raceTime = raceTime, raceType = raceType))) {
            println("Race added successfully!")
        } else {
            println("Failed to add race. Try again!")
        }
    }
}

/**
 * Displays a menu to list swimmers based on different criteria (all, active, or archived).
 * Executes the appropriate function based on the user's input, or displays an error message if no swimmers are stored.
 */
fun listSwimmers() {
    if (swimmerAPI.numberOfSwimmers() > 0) {
        val green = "\u001B[32m"
        val yellow = "\u001B[33m"
        val reset = "\u001B[0m"

        val option = readNextInt(
            """
            $yellow┌─────────────────────────────────────┐
            │         ${green}VIEW SWIMMERS MENU$yellow          │
            ├─────────────────────────────────────┤
            │   1) ${green}View All Swimmers$reset              │
            │   2) ${green}View Active Swimmers$reset           │
            │   3) ${green}View Archived Swimmers$reset         │
            └─────────────────────────────────────┘
            ==>> 
            """.trimIndent()
        )

        when (option) {
            1 -> listAllSwimmers()
            2 -> listActiveSwimmers()
            3 -> listArchivedSwimmers()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No notes stored")
    }
}
/**
 * Lists all swimmers in the swimmerAPI.
 */
fun listAllSwimmers() = println(swimmerAPI.listAllSwimmers())

/**
 * Lists all active swimmers in the swimmerAPI.
 */
fun listActiveSwimmers() = println(swimmerAPI.listActiveSwimmers())

/**
 * Lists all archived swimmers in the swimmerAPI.
 */
fun listArchivedSwimmers() = println(swimmerAPI.listArchivedSwimmers())

/**
 * Lists all swimmers and prompts the user to update the swimmer details (name, level, and category) for the selected swimmer.
 * Updates the swimmer in the swimmerAPI if the entered ID is valid, and displays a success or failure message.
 */
fun updateSwimmer() {
    listSwimmers()
    if (swimmerAPI.numberOfSwimmers() > 0) {

        val id = readNextInt("Enter the id of the note to update: ")
        if (swimmerAPI.findSwimmer(id) != null) {
            val swimmerName = ScannerInput.readNextLine("Enter a title for the note: ")
            val swimmerLevel = readNextInt("Enter a level of swimmer (1-low, 2, 3, 4, 5-high): ")
            val swimmerCategory = ScannerInput.readNextLine("Enter a category for the note: ")

            if (swimmerAPI.update(id, Swimmer(0, swimmerName, swimmerLevel, swimmerCategory, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

/**
 * Lists all active swimmers and prompts the user to enter the ID of the swimmer to archive.
 * If a valid ID is entered, attempts to archive the swimmer in the swimmerAPI and displays a success or failure message.
 */
fun archiveSwimmer() {
    listActiveSwimmers()
    if (swimmerAPI.numberOfActiveSwimmers() > 0) {
        // only ask the user to choose the swimmer to archive if active swimmers exist
        val id = readNextInt("Enter the ID of the swimmer you want to archive: ")
        val archivedSwimmer = swimmerAPI.archiveSwimmer(id)
        if (archivedSwimmer) {
            println("Swimmer with ID $id has been successfully archived!")
        } else {
            println("Failed to archive swimmer with ID $id. Please try again.")
        }
    } else {
        println("There are no active swimmers to archive.")
    }
}

/**
 * Deletes a race from a swimmer's list of races.
 * Prompts the user to select an active swimmer, then a race from that swimmer's list.
 * If the race is found and successfully deleted, a success message is printed.
 */
fun deleteRace() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        val race: Race? = askUserToChooseRace(swimmer)
        if (race != null) {
            val isDeleted = swimmer.delete(race.raceId)
            if (isDeleted)
                println("Race deleted successfully from ${swimmer.swimmerName}'s list of races.")

            else println("Unable to delete the race from ${swimmer.swimmerName}'s list of races.")
        } else println("Invalid race ID. Please choose a valid race ID.")
    }
}

/**
 * Updates the graded contents of a race in a swimmer's list.
 * Prompts the user to select an active swimmer, then a race from that swimmer's list.
 * Asks the user for new graded contents and updates the race if found.
 */
fun updateRaceGradedInSwimmer() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()

    if (swimmer != null) {
        val item: Race? = askUserToChooseRace(swimmer)

        if (item != null) {
            val newGrade = ScannerInput.readNextLine("Enter new grade(eg.Pass, Fail): ")
            val newTime = ScannerInput.readNextLine("Enter new time(use format HH:mm:ss): ")
            val newType = ScannerInput.readNextLine("Enter new type(eg. Backstroke, Freestyle etc.): ")

            val updatedRace = Race(
                raceId = item.raceId,
                raceGraded = newGrade,
                raceTime = newTime,
                raceType = newType,
                isRaceOutdated = item.isRaceOutdated
            )

            if (swimmer.update(item.raceId, updatedRace)) {
                println("Race contents updated")
            } else {
                println("Race contents NOT updated")
            }
        } else {
            println("Invalid Race Id")
        }
    }
}

/**
*
*Prompts the user to choose an active swimmer from the list of active swimmers and returns the corresponding Swimmer object.
*Verifies if the selected swimmer is active before returning the object. If the swimmer is not active, it returns null.
*@return Swimmer? - the selected swimmer object if active, null otherwise
 */
fun markRaceStatus() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        val race: Race? = askUserToChooseRace(swimmer)
        if (race != null) {
            var changeStatus: Char
            if (race.isRaceOutdated) {
                changeStatus =
                    ScannerInput.readNextChar("The race is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    race.isRaceOutdated = false
            } else {
                changeStatus =
                    ScannerInput.readNextChar("The race is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    race.isRaceOutdated = true
            }
        }
    }
}

/**
 * Asks the user to choose an active swimmer from the list of active swimmers.
 * Prompts the user to enter the ID of the swimmer they want to select.
 * Verifies if the swimmer is active and returns the swimmer object if found and active.
 *
 * @return Swimmer? - the selected swimmer object if active, null otherwise
 */
private fun askUserToChooseActiveSwimmer(): Swimmer? {
    listActiveSwimmers()
    if (swimmerAPI.numberOfActiveSwimmers() > 0) {
        val swimmer = swimmerAPI.findSwimmer(readNextInt("\nEnter the id of the swimmer: "))
        if (swimmer != null) {
            if (swimmer.isSwimmerArchived) {
                println("Swimmer is NOT Active, it is Archived")
            } else {
                return swimmer
            }
        } else {
            println("Swimmer id is not valid")
        }
    }
    return null
}

/**
 * Asks the user to choose a race from a swimmer's list of races.
 * Prints the list of races for the chosen swimmer and prompts the user to enter the ID of the race.
 * Returns the selected race object if found, null otherwise.
 *
 * @param swimmer - the swimmer object whose races the user will choose from
 * @return Race? - the selected race object if found, null otherwise
 */
private fun askUserToChooseRace(swimmer: Swimmer): Race? {
    if (swimmer.numberOfRaces() > 0) {
        print(swimmer.listRaces())
        return swimmer.findOne(readNextInt("\nEnter the id of the item: "))
    } else {
        println("No items for chosen swimmer")
        return null
    }
}

/**
 * Exits the application and prints a farewell message.
 */
fun exitApp() {
    println("Exiting Application")
    exitProcess(0)
}

/**
 * Saves the current state of the swimmerAPI data to a file.
 * Handles exceptions that might occur during the save operation.
 */
fun save() {
    try {
        swimmerAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Loads the swimmerAPI data from a file.
 * Handles exceptions that might occur during the load operation.
 */
fun load() {
    try {
        swimmerAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
