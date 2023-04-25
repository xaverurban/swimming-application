

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
fun main(args: Array<String>) {
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
    return readNextInt(
        """ 
         > -----------------------------------------------------  
         > |                  SWIMMING APP                     |
         > -----------------------------------------------------  
         > | NOTE MENU                                         |
         > |   1) Add a Swimmer                                |
         > |   2) List Swimmers                                |
         > |   3) Update a Swimmer                             |
         > |   4) Delete a Swimmer                             |
         > |   5) Archive a Swimmer                            |
         > -----------------------------------------------------  
         > | SWIMMER MENU                                      | 
         > |   6) Add race to a Swimmer                        |
         > |   7) Update race contents on a Swimmer            |
         > |   8) Delete race from a Swimmer                   |
         > |   9) Mark item as                                 | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR SWIMMERS                          | 
         > |   10) Search for all Swimmers (by name)           |
               -Under Excavation-
         > -----------------------------------------------------  
         > | REPORT MENU FOR RACES                             |                                
         > |   15) Search for all races (by item description)  |
         > |   16) List graded                                 |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
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
            //   9 -> markRaceStatus()
            //   10 -> searchSwimmers()
            //     15 -> searchRaces()
            //  16 -> listGradedRaces()
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
    val swimmerName = ScannerInput.readNextLine("Enter a title for the swimmer: ")
    val swimmerLevel = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
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
        if (swimmer.addRace(Race(raceGraded = ScannerInput.readNextLine("\t Race Grade: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

/**
 * Displays a menu to list swimmers based on different criteria (all, active, or archived).
 * Executes the appropriate function based on the user's input, or displays an error message if no swimmers are stored.
 */
fun listSwimmers() {
    if (swimmerAPI.numberOfSwimmers() > 0) {
        val option = readNextInt(
            """
    ┌─────────────────────────────────────┐
    │         VIEW SWIMMERS MENU          │
    ├─────────────────────────────────────┤
    │   1) View All Swimmers              │
    │   2) View Active Swimmers           │
    │   3) View Archived Swimmers         │
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
            val swimmerLevel = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
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
            val newGrade = ScannerInput.readNextLine("Enter new contents: ")
            if (swimmer.update(item.raceId, Race(raceGraded = newGrade))) {
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
private fun askUserToChooseRace(swimmer: Swimmer?): Race? {
    return if (swimmer?.numberOfRaces()!! > 0) {
        print(swimmer?.listRaces())
        swimmer?.findOne(readNextInt("\nEnter the id of the item: "))
    } else {
        println("No items for chosen swimmer")
        null
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
