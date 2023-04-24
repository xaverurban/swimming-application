package controllers

import models.Swimmer
import models.Race
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SwimmerAPITest {

    private var michael: Swimmer? = null
    private var sarah: Swimmer? = null
    private var tom: Swimmer? = null

    private var populatedSwimmers: SwimmerAPI? = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
    private var emptySwimmers: SwimmerAPI? = SwimmerAPI(XMLSerializer(File("swimmers.xml")))

    @BeforeEach
    fun setup() {
        michael = Swimmer(1, "Michael", 5, "Elite")
        sarah = Swimmer(2, "Sarah", 3, "Intermediate")
        tom = Swimmer(3, "Tom", 1, "Beginner")

        michael!!.races.add(Race(1, "no"))
        sarah!!.races.add(Race(2, "yes"))
        tom!!.races.add(Race(3, "no"))

        // Adding Swimmers to the SwimmerAPI
        populatedSwimmers!!.add(michael!!)
        populatedSwimmers!!.add(sarah!!)
        populatedSwimmers!!.add(tom!!)
    }

    @AfterEach
    fun tearDown() {
        michael = null
        sarah = null
        tom = null
        populatedSwimmers = null
        emptySwimmers = null
    }

}