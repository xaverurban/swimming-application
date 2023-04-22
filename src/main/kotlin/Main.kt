

import models.Swimmer
import utils.ScannerInput
import java.lang.System.exit
import models.Race
import kotlin.system.exitProcess



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
            1  -> addSwimmer()
            2  -> listSwimmers()
            3  -> updateSwimmer()
            4  -> deleteSwimmer()
            0  -> exitApp()
            else -> println("Invalid option entered: $option")

        }
    } while (true)
}
fun addSwimmer(){
    println("placeholder")
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



private fun askUserToChooseRace(note: Swimmer): Race? {
    return if (note.numberOfRaces() > 0) {
        print(note.listRaces())
        note.findOne(ScannerInput.readNextInt("\nEnter the id of the item: "))
    }
    else{
        println ("No items for chosen note")
        null
    }
}
fun exitApp(){
    println("Exiting Application")
    exitProcess(0)
}