

import utils.ScannerInput
import java.lang.System.exit

import kotlin.system.exitProcess



fun main(args: Array<String>) {
    runMenu()

}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        SWIMMER  APP            |
         > ----------------------------------
         > |  MENU                          |
         > |   1) Add a swimmer             |
         > |   2) List all swimmers         |
         > |   3) Update a swimmer          |
         > |   4) Delete a swimmer          |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
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

fun exitApp(){
    println("Exiting Application")
    exitProcess(0)
}