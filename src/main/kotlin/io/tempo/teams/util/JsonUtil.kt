package io.tempo.teams.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParseException
import kotlin.Throws
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Utility methods used all around.
 *
 * @author marcellodesales
 * @author leandromsales
 */
object JsonUtil {

    // TODO: this will not use the timezone defined in the yaml file, but it will use the system timezone.
    private val MAPPER = ObjectMapper()
    private val LOG = LoggerFactory.getLogger(JsonUtil::class.java)

    init {
        val h5module = Hibernate5Module()
        h5module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING)
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS)
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT)
        MAPPER.registerModule(h5module)
        LOG.debug("Loaded JsonUtil with Hibernate 5 module enabled.")
    }

    /**
     * @param o An object instance from a POJO implementation.
     * @return The JSON representation of the given O.
     * @throws JsonProcessingException
     */
    @Throws(JsonProcessingException::class)
    fun toJson(o: Any?): String {
        return MAPPER.writeValueAsString(o)
    }

    /**
     * @param o An object instance from a POJO implementation.
     * @return The JSON representation of the given O.
     * @throws JsonProcessingException
     */
    @Throws(JsonProcessingException::class)
    fun toJsonFile(o: Any?, filePath: File) {
        val json: String = MAPPER.writeValueAsString(o)
        filePath.bufferedWriter().use { out ->
            out.write(json)
        }
    }

    /**
     * @param o An object instance from a POJO implementation.
     * @return The JSON representation of the given O.
     * @throws JsonProcessingException
     */
    @Throws(JsonProcessingException::class)
    fun toFile(o: String, filePath: File) {
        filePath.bufferedWriter().use { out ->
            out.write(o)
        }
    }

    /**
     * Retrieves an object from a given file instance. More details at
     * http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
     *
     * @param <T> is the type of the class to be deserialized.
     * @param jsonObject the object in Json format.
     * @param clazz the class to convert.
     * @return The instance of the given class.
    </T> */
    fun <T> toObject(jsonObject: String?, clazz: Class<T>): T? {
        return try {
            MAPPER.readValue(jsonObject, clazz)

        } catch (errorDeserializing: JsonProcessingException) {
            LOG.error("While deserializing json String {} {}", jsonObject, errorDeserializing)
            null
        }
    }

    /**
     * Retrieves an object from a given file instance. More details at
     * http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
     *
     * @param <T> is the type of the class to be deserialized.
     * @param clazz the class to convert.
     * @return The instance of the given class.
    </T> */
    fun <T> toObject(buffer: Optional<ByteBuffer?>, clazz: Class<T>): T? {
        var jsonObject: String? = null
        return try {
            if (buffer.isPresent) {
                jsonObject = StandardCharsets.UTF_8.decode(buffer.get()).toString()
                return MAPPER.readValue(jsonObject, clazz)
            }
            null

        } catch (errorDeserializing: JsonProcessingException) {
            LOG.error("While deserializing json String {} {}", jsonObject, errorDeserializing)
            null
        }
    }

    /**
     * Retrieves an object from a given file instance. More details at
     * http://www.mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
     *
     * @param <T> is the type of the class to be deserialized.
     * @param file the file path in the fs
     * @param clazz the class to convert.
     * @return The instance of the given class.
     * @throws IOException If any error occurs while loading the file.
     * @throws JsonMappingException  if any mapping error occurs.
     * @throws JsonParseException if any json format error occurs.
    </T> */
    @Throws(JsonParseException::class, JsonMappingException::class, IOException::class)
    fun <T> toObject(file: File?, clazz: Class<T>): T {
        return MAPPER.readValue(file, clazz)
    }

}