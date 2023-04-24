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
        michael = Swimmer(1, "Michael", 5, "Elite",true)
        sarah = Swimmer(2, "Sarah", 3, "Intermediate",false)
        tom = Swimmer(3, "Tom", 1, "Beginner",true)

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

    @Nested
    inner class AddSwimmers {
        @Test
        fun `adding a Swimmer to a populated list adds to ArrayList`() {
            val newSwimmer = Swimmer(4, "Emma", 2, "Intermediate")
            assertEquals(3, populatedSwimmers!!.numberOfSwimmers())
            assertTrue(populatedSwimmers!!.add(newSwimmer))
            assertEquals(4, populatedSwimmers!!.numberOfSwimmers())
            assertEquals(newSwimmer, populatedSwimmers!!.findSwimmer(newSwimmer.swimmerId))
        }

        @Test
        fun `adding a Swimmer to an empty list adds to ArrayList`() {
            val newSwimmer = Swimmer(5, "Lucas", 1, "Beginner")
            assertEquals(0, emptySwimmers!!.numberOfSwimmers())
            assertTrue(emptySwimmers!!.add(newSwimmer))
            assertEquals(1, emptySwimmers!!.numberOfSwimmers())
            assertEquals(newSwimmer, emptySwimmers!!.findSwimmer(newSwimmer.swimmerId))
        }
    }
    @Nested
    inner class ListSwimmers {

        @Test
        fun `listAllSwimmers returns No Swimmers Stored message when ArrayList is empty`() {
            assertEquals(0, emptySwimmers!!.numberOfSwimmers())
            assertTrue(emptySwimmers!!.listAllSwimmers().lowercase().contains("no swimmer"))
        }

        @Test
        fun `listAllSwimmers returns Swimmers when ArrayList has swimmers stored`() {
            assertEquals(3, populatedSwimmers!!.numberOfSwimmers())
            val swimmersString = populatedSwimmers!!.listAllSwimmers().lowercase()
            assertTrue(swimmersString.contains("michael"))
            assertTrue(swimmersString.contains("sarah"))
            assertTrue(swimmersString.contains("tom")) // Changed from "john" to "tom"
        }

        @Test
        fun `listActiveSwimmers returns no active swimmers stored when ArrayList is empty`() {
            assertEquals(0, emptySwimmers!!.numberOfActiveSwimmers())
            assertTrue(
                emptySwimmers!!.listActiveSwimmers().lowercase().contains("no active swimmers")
            )
        }

        @Test
        fun `listActiveSwimmers returns active swimmers when ArrayList has active swimmers stored`() {
            assertEquals(1, populatedSwimmers!!.numberOfActiveSwimmers())
            val activeSwimmersString = populatedSwimmers!!.listActiveSwimmers().lowercase()
            assertFalse(activeSwimmersString.contains("michael"))
            assertTrue(activeSwimmersString.contains("sarah"))
            assertFalse(activeSwimmersString.contains("tom"))
        }

        @Test
        fun `listArchivedSwimmers returns no archived swimmers when ArrayList is empty`() {
            assertEquals(0, emptySwimmers!!.numberOfArchivedSwimmers())
            assertTrue(
                emptySwimmers!!.listArchivedSwimmers().lowercase().contains("no archived swimmers")
            )
        }

        @Test
        fun `listArchivedSwimmers returns archived swimmers when ArrayList has archived swimmers stored`() {
            assertEquals(2, populatedSwimmers!!.numberOfArchivedSwimmers())
            val archivedSwimmersString = populatedSwimmers!!.listArchivedSwimmers().lowercase()
            assertTrue(archivedSwimmersString.contains("michael"))
            assertFalse(archivedSwimmersString.contains("sarah"))
            assertTrue(archivedSwimmersString.contains("tom"))
        }
    }
}

