

import controllers.SwimmerAPI
import models.Swimmer
import utils.ScannerInput
import models.Race
import kotlin.system.exitProcess

private val swimmerAPI = SwimmerAPI()

fun main(args: Array<String>) {
    runMenu()

}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
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
            2 -> listSwimmers()
            3 -> updateSwimmer()
            4 -> deleteSwimmer()
           // 5 -> archiveSwimmer()
            6 -> addRaceToSwimmer()
            7 -> updateRaceGradedInSwimmer()
           // 8 -> deleteRace()
         //   9 -> markItemStatus()
         //   10 -> searchSwimmers()
       //     15 -> searchRaces()
          //  16 -> listGradedRaces()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")

        }
    } while (true)
}
fun addSwimmer() {
    val swimmerName = ScannerInput.readNextLine("Enter a title for the swimmer: ")
    val swimmerLevel = ScannerInput.readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val swimmerCategory = ScannerInput.readNextLine("Enter a category for the swimmer: ")
    val isAdded = swimmerAPI.add(Swimmer(swimmerName = swimmerName, swimmerLevel = swimmerLevel, swimmerCategory = swimmerCategory))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}
fun listSwimmers(){
    println("placeholder")
}
fun updateSwimmer(){
    println("placeholder")
}

fun deleteSwimmer(){
    println("placeholder")
}
private fun addRaceToSwimmer() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        if (swimmer.addRace(Race(raceGraded = ScannerInput.readNextLine("\t Race Grade: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}


fun listActiveSwimmers() = println(swimmerAPI.listActiveSwimmers())


fun updateRaceGradedInSwimmer() {
    val swimmer: Swimmer? = askUserToChooseActiveSwimmer()
    if (swimmer != null) {
        val item: Race? = askUserToChooseRace(swimmer)
        if (item != null) {
            val newGrade = ScannerInput.readNextLine("Enter new contents: ")
            if (swimmer.update(item.raceId, Race(raceGraded = newGrade))) {
                println("Item contents updated")
            } else {
                println("Item contents NOT updated")
            }
        } else {
            println("Invalid Item Id")
        }
    }
}

private fun askUserToChooseActiveSwimmer(): Swimmer? {
    listActiveSwimmers()
    if (swimmerAPI.numberOfActiveSwimmers() > 0) {
        val swimmer = swimmerAPI.findSwimmer(ScannerInput.readNextInt("\nEnter the id of the swimmer: "))
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
        swimmer?.findOne(ScannerInput.readNextInt("\nEnter the id of the item: "))
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