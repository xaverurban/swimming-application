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
        michael = Swimmer(1, "Michael", 5, "Backstroke", true)
        sarah = Swimmer(2, "Sarah", 3, "Freestyle", false)
        tom = Swimmer(3, "Tom", 1, "Butterfly", true)

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
            assertTrue(swimmersString.contains("tom"))
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
            assertTrue(archivedSwimmersString.contains("michael"))  // assert true/false messed up <-- come back and fix after dinner
            assertFalse(archivedSwimmersString.contains("sarah"))
            assertTrue(archivedSwimmersString.contains("tom"))
        }
    }

    @Nested
    inner class UpdateSwimmers {
        @Test
        fun `updating a swimmer that does not exist returns false`() {
            assertFalse(populatedSwimmers!!.update(6, Swimmer(6, "Nonexistent", 2, "Freestyle", false)))
            assertFalse(populatedSwimmers!!.update(-1, Swimmer(-1, "Nonexistent", 2, "Freestyle", false)))
            assertFalse(emptySwimmers!!.update(0, Swimmer(0, "Nonexistent", 2, "Freestyle", false)))
        }

        @Test
        fun `updating a swimmer that exists returns true and updates`() {
            // Check if swimmer with ID 1 exists and verify its properties
            assertEquals(sarah, populatedSwimmers!!.findSwimmer(1))
            assertEquals("Sarah", populatedSwimmers!!.findSwimmer(1)!!.swimmerName)
            assertEquals(3, populatedSwimmers!!.findSwimmer(1)!!.swimmerLevel)
            assertEquals("Freestyle", populatedSwimmers!!.findSwimmer(1)!!.swimmerCategory)

            // Update swimmer with ID 1 and ensure its properties are updated successfully
            assertTrue(populatedSwimmers!!.update(1, Swimmer(1, "Sarah M", 5, "Backstroke", false)))
            assertEquals("Sarah M", populatedSwimmers!!.findSwimmer(1)!!.swimmerName)
            assertEquals(5, populatedSwimmers!!.findSwimmer(1)!!.swimmerLevel)
            assertEquals("Backstroke", populatedSwimmers!!.findSwimmer(1)!!.swimmerCategory)
        }

    }
    @Nested
    inner class DeleteSwimmers {

        @Test
        fun `deleting a Swimmer that does not exist returns false`() {
            assertFalse(emptySwimmers!!.delete(0))
            assertFalse(populatedSwimmers!!.delete(-1))
            assertFalse(populatedSwimmers!!.delete(4))
        }

        @Test
        fun `deleting a swimmer that exists deletes and returns true`() {
            assertEquals(3, populatedSwimmers!!.numberOfSwimmers())
            assertTrue(populatedSwimmers!!.delete(1))
            assertEquals(2, populatedSwimmers!!.numberOfSwimmers())
            assertTrue(populatedSwimmers!!.delete(2))
            assertEquals(1, populatedSwimmers!!.numberOfSwimmers())
        }
    }



}


