package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Race
import models.Swimmer
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * XMLSerializer is a class that implements the Serializer interface for reading and writing
 * Swimmer and Race objects to and from an XML file.
 *
 * @property file The file where the serialized data will be stored.
 */
class XMLSerializer(private val file: File) : Serializer {

    /**
     * Reads serialized Swimmer and Race objects from the XML file.
     * @return Deserialized Swimmer and Race objects.
     * @throws Exception If an error occurs during reading the file or deserializing the objects.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Swimmer::class.java, Race::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes serialized Swimmer and Race objects to the XML file.
     * @param obj The Swimmer and Race objects to be serialized.
     * @throws Exception If an error occurs during writing to the file or serializing the objects.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Swimmer::class.java, Race::class.java))
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}

