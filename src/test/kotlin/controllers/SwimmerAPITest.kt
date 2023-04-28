import controllers.SwimmerAPI
import models.Race
import models.Swimmer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

        michael!!.races.add(Race(1, "no", "14:00","Backstroke",false))
        sarah!!.races.add(Race(2, "yes", "15:00","Butterfly",true))
        tom!!.races.add(Race(3, "no", "16:00","Backstroke",false))

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
            assertTrue(archivedSwimmersString.contains("michael"))
            assertFalse(archivedSwimmersString.contains("sarah"))
            assertTrue(archivedSwimmersString.contains("tom"))
        }
        @Test
        fun `listUngradedRaces returns no swimmer stored message when no swimmers exist`() {
            val result = emptySwimmers!!.listUngradedRaces()
            assertTrue(result.lowercase().contains("no swimmer stored"))
        }

        @Test
        fun `listUngradedRaces returns correct list of ungraded races`() {
            val result = populatedSwimmers!!.listUngradedRaces()

            assertTrue(result.contains("Michael: no"))
            assertFalse(result.contains("Sarah: yes"))
            assertTrue(result.contains("Tom: no"))

            assertFalse(result.contains("yes"))
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
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty swimmers.XML file.
            val storingSwimmers = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
            storingSwimmers.store()

            // Loading the empty swimmers.xml file into a new object
            val loadedSwimmers = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
            loadedSwimmers.load()

            // Comparing the source of the swimmers (storingSwimmers) with the XML loaded swimmers (loadedSwimmers)
            assertEquals(0, storingSwimmers.numberOfSwimmers())
            assertEquals(0, loadedSwimmers.numberOfSwimmers())
            assertEquals(storingSwimmers.numberOfSwimmers(), loadedSwimmers.numberOfSwimmers())
        }

        // Seem to not be saving the race model, come back and fix, probably problem in xml serializer?
        @Test
        fun `saving and loading a loaded collection in XML doesn't lose data`() {
            // Storing 3 swimmers to the swimmers.XML file.
            val storingSwimmers = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
            storingSwimmers.add(michael!!)
            storingSwimmers.add(sarah!!)
            storingSwimmers.add(tom!!)
            michael!!.races.add(Race(1, "no","14:13","Freestyle",false))
            storingSwimmers.store()

            // Loading swimmers.xml into a different collection
            val loadedSwimmers = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
            loadedSwimmers.load()

            // Comparing the source of the swimmers (storingSwimmers) with the XML loaded swimmers (loadedSwimmers)
            assertEquals(3, storingSwimmers.numberOfSwimmers())
            assertEquals(3, loadedSwimmers.numberOfSwimmers())
            assertEquals(storingSwimmers.numberOfSwimmers(), loadedSwimmers.numberOfSwimmers())
            assertEquals(storingSwimmers.findSwimmer(1), loadedSwimmers.findSwimmer(1))
            assertEquals(storingSwimmers.findSwimmer(2), loadedSwimmers.findSwimmer(2))
            assertEquals(storingSwimmers.findSwimmer(3), loadedSwimmers.findSwimmer(3))
        }
    }
    @Nested
    inner class ArchiveSwimmers {
        @Test
        fun `archiving a swimmer that does not exist returns false`() {
            assertFalse(populatedSwimmers!!.archiveSwimmer(6))
            assertFalse(populatedSwimmers!!.archiveSwimmer(-1))
            assertFalse(emptySwimmers!!.archiveSwimmer(0))
            assertFalse(populatedSwimmers!!.archiveSwimmer(99))
            assertTrue(populatedSwimmers!!.archiveSwimmer(1))
        }

        @Test
        fun `archiving an already archived swimmer returns false`() {
            assertTrue(populatedSwimmers!!.findSwimmer(2)!!.isSwimmerArchived)
            assertFalse(populatedSwimmers!!.archiveSwimmer(2))
        }

        @Test
        fun `archiving an active swimmer that exists returns true and archives`() {
            assertFalse(populatedSwimmers!!.findSwimmer(1)!!.isSwimmerArchived)
            assertTrue(populatedSwimmers!!.archiveSwimmer(1))
            assertTrue(populatedSwimmers!!.findSwimmer(1)!!.isSwimmerArchived)
        }
    }
    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfSwimmersCalculatedCorrectly() {
            assertEquals(3, populatedSwimmers!!.numberOfSwimmers())
            assertEquals(0, emptySwimmers!!.numberOfSwimmers())
        }

        @Test
        fun numberOfArchivedSwimmersCalculatedCorrectly() {
            assertEquals(2, populatedSwimmers!!.numberOfArchivedSwimmers())
            assertEquals(0, emptySwimmers!!.numberOfArchivedSwimmers())
        }

        @Test
        fun numberOfActiveSwimmersCalculatedCorrectly() {
            assertEquals(1, populatedSwimmers!!.numberOfActiveSwimmers())
            assertEquals(0, emptySwimmers!!.numberOfActiveSwimmers())
        }

        @Test
        fun numberOfSwimmersByLevelCalculatedCorrectly() {
            assertEquals(1, populatedSwimmers!!.numberOfSwimmersByLevel(1))
            assertEquals(0, populatedSwimmers!!.numberOfSwimmersByLevel(2))
            assertEquals(1, populatedSwimmers!!.numberOfSwimmersByLevel(3))
            assertEquals(0, populatedSwimmers!!.numberOfSwimmersByLevel(4))
            assertEquals(1, populatedSwimmers!!.numberOfSwimmersByLevel(5))
            assertEquals(0, emptySwimmers!!.numberOfSwimmersByLevel(1))
        }
        @Test
        fun `numberOfUngradedRaces returns correct count of non-outdated races`() {
            val numberOfUngradedRaces = populatedSwimmers!!.numberOfUngradedRaces()
            val expectedNumberOfUngradedRaces = 2

            assertEquals(expectedNumberOfUngradedRaces, numberOfUngradedRaces)
        }
    }
    @Nested
    inner class SearchMethods {

        @Test
        fun `search swimmers by name returns no swimmers when no swimmers with that name exist`() {
            assertEquals(3, populatedSwimmers!!.numberOfSwimmers())
            val searchResults = populatedSwimmers!!.searchByName("no results expected")
            assertTrue(searchResults.isEmpty())

            assertEquals(0, emptySwimmers!!.numberOfSwimmers())
            assertTrue(emptySwimmers!!.searchByName("").isEmpty())
        }

        @Test
        fun `search swimmers by name returns swimmers when swimmers with that name exist`() {
            assertEquals(3, populatedSwimmers?.numberOfSwimmers())

            populatedSwimmers?.let { swimmers ->
                var searchResults = swimmers.searchByName("Michael")
                assertTrue(searchResults.contains(swimmers.findSwimmer(0)))
                assertFalse(searchResults.contains(swimmers.findSwimmer(2)))

                searchResults = swimmers.searchByName("sarah")
                assertTrue(searchResults.contains(swimmers.findSwimmer(1)))
                assertFalse(searchResults.contains(swimmers.findSwimmer(2)))

                searchResults = swimmers.searchByName("a")
                assertTrue(searchResults.contains(swimmers.findSwimmer(1)))
                assertTrue(searchResults.contains(swimmers.findSwimmer(0)))
                assertFalse(searchResults.contains(swimmers.findSwimmer(3)))
            }
        }
        @Test
        fun `searchRaceByContents returns no swimmer stored message when no swimmers exists`() {
            val result = emptySwimmers!!.searchRaceByContents("abed")
            assertTrue(result.lowercase().contains("no swimmer stored"))
        }

        @Test
        fun `searchRaceByContents returns no races found message when no races exist`() {
            val result = populatedSwimmers!!.searchRaceByContents("popcorn")
            assertTrue(result.lowercase().contains("no races found for: popcorn"))
        }

        @Test
        fun `searchRaceByContents returns race with the given contents`() {
            val newRace = Race(4, "medley","16:14","medley",false)
            michael!!.races.add(newRace)

            val result = populatedSwimmers!!.searchRaceByContents("medley")
            assertTrue(result.contains("Michael"))
            assertTrue(result.contains("medley"))
            assertFalse(result.contains("no"))
            assertFalse(result.contains("yes"))
        }
        @Test
        fun `searchSwimmersByName returns empty list when nothing matching swimmer exists`() {

            val api = SwimmerAPI(XMLSerializer(File("swimmers.xml")))
            api.add(Swimmer(1, "Michael", 5, "Backstroke", true))
            api.add(Swimmer(2, "Sarah", 3, "Freestyle", false))
            api.add(Swimmer(3, "Tom", 1, "Butterfly", true))

            val searchResults = api.searchSwimmersByName("nonexistent")

            assertTrue(searchResults.isEmpty())
        }
    }
}
