

import controllers.SwimmerAPI
import models.Swimmer
import utils.ScannerInput
import models.Race
import utils.ScannerInput.readNextInt
import java.io.File
import kotlin.system.exitProcess
import persistence.XMLSerializer
private val swimmerAPI = SwimmerAPI(XMLSerializer(File("swimmers.xml")))

fun main(args: Array<String>) {
    runMenu()

}

fun mainMenu() : Int {
    return readNextInt(""" 
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
         > ==>> """.trimMargin(">"))
}




fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addSwimmer()
            2 -> listAllSwimmers()
            3 -> updateSwimmer()
            4 -> deleteSwimmer()
            5 -> archiveSwimmer()
            6 -> addRaceToSwimmer()
            7 -> updateRaceGradedInSwimmer()
            8 -> deleteRace()
            //   9 -> markItemStatus()
            //   10 -> searchSwimmers()
            //     15 -> searchRaces()
            //  16 -> listGradedRaces()
            17 ->save()
            18 ->load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")

        }
    } while (true)
}
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


private fun addRaceToSwimmer() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        if (swimmer.addRace(Race(raceGraded = ScannerInput.readNextLine("\t Race Grade: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun listSwimmers() {
    if (swimmerAPI.numberOfSwimmers() > 0) {
        val option = readNextInt("""
    ┌─────────────────────────────────────┐
    │         VIEW SWIMMERS MENU          │
    ├─────────────────────────────────────┤
    │   1) View All Swimmers              │
    │   2) View Active Swimmers           │
    │   3) View Archived Swimmers         │
    └─────────────────────────────────────┘
    ==>> """.trimIndent())

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
fun listAllSwimmers() = println(swimmerAPI.listAllSwimmers())
fun listActiveSwimmers() = println(swimmerAPI.listActiveSwimmers())
fun listArchivedSwimmers() = println(swimmerAPI.listArchivedSwimmers())

fun updateSwimmer() {
    listSwimmers()
    if (swimmerAPI.numberOfSwimmers() > 0) {

        val id = readNextInt("Enter the id of the note to update: ")
        if (swimmerAPI.findSwimmer(id) != null) {
            val swimmerName = ScannerInput.readNextLine("Enter a title for the note: ")
            val swimmerLevel = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val swimmerCategory = ScannerInput.readNextLine("Enter a category for the note: ")

            // pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (swimmerAPI.update(id, Swimmer(0,swimmerName, swimmerLevel, swimmerCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

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

private fun askUserToChooseActiveSwimmer(): Swimmer? {
    listActiveSwimmers()
    if (swimmerAPI.numberOfActiveSwimmers() > 0) {
        val swimmer = swimmerAPI.findSwimmer(readNextInt("\nEnter the id of the swimmer: "))
        if (swimmer != null) {
            if (swimmer.isSwimmerArchived) {
                println("Swimmer is NOT Active, it is Archived")
            } else {
                return swimmer //chosen swimmer is active
            }
        } else {
            println("Swimmer id is not valid")
        }
    }
    return null //selected swimmer is not active
}

private fun askUserToChooseRace(swimmer: Swimmer?): Race? {
    return if (swimmer?.numberOfRaces()!! > 0) {
        print(swimmer?.listRaces())
        swimmer?.findOne(readNextInt("\nEnter the id of the item: "))
    }
    else{
        println ("No items for chosen swimmer")
        null
    }
}
fun exitApp(){
    println("Exiting Application")
    exitProcess(0)
}
fun save() {
    try {
        swimmerAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        swimmerAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}