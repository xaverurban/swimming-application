package controllers

import models.Swimmer
import utils.Utilities.formatListString
import java.util.ArrayList

class SwimmerAPI() {

    private var swimmers = ArrayList<Swimmer>()


    private var lastId = 0
    private fun getId() = lastId++


    fun add(swimmer: Swimmer): Boolean {
        swimmer.swimmerId = getId()
        return swimmers.add(swimmer)
    }

    fun delete(id: Int) = swimmers.removeIf { swimmer -> swimmer.swimmerId == id }

    fun update(id: Int, swimmer: Swimmer?): Boolean {
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

    
    fun listActiveSwimmers() =
        if (numberOfActiveSwimmers() == 0) "No active swimmer stored"
        else formatListString(swimmers.filter { swimmer -> !swimmer.isSwimmerArchived })

    fun listArchivedSwimmers() =
        if (numberOfArchivedSwimmers() == 0) "No archived swimmer stored"
        else formatListString(swimmers.filter { swimmer -> swimmer.isSwimmerArchived })


    fun numberOfSwimmers() = swimmers.size
    fun numberOfArchivedSwimmers(): Int = swimmers.count { swimmer: Swimmer -> swimmer.isSwimmerArchived }
    fun numberOfActiveSwimmers(): Int = swimmers.count { swimmer: Swimmer -> !swimmer.isSwimmerArchived }


    fun findSwimmer(swimmerId : Int) =  swimmers.find{ swimmer -> swimmer.swimmerId == swimmerId }



}