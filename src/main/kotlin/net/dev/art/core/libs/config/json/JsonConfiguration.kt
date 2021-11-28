package net.dev.art.core.libs.config.json

import java.util.stream.Collectors.toMap
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.logging.Level
import org.apache.commons.lang.Validate
import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

class JsonConfiguration : FileConfiguration() {
    companion object {
        private var GSON: Gson? = null
        fun loadConfiguration(file: File): JsonConfiguration {
            Validate.notNull(file, "File cannot be null")
            val config = JsonConfiguration()
            try {
                config.load(file)
            } catch (e: IOException) {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load $file", e)
            } catch (e: InvalidConfigurationException) {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load $file", e)
            }
            return config
        }

        private fun serializeMap(
            src: Map<String, Any>,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            val `object` = JsonObject()
            for ((key, value) in src) {
                `object`.add(key, serializeValue(value, context))
            }
            return `object`
        }

        private fun serializeArray(src: List<Any>, srcOfType: Type, context: JsonSerializationContext): JsonElement {
            val array = JsonArray()
            for (`object` in src) {
                array.add(serializeValue(`object`, context))
            }
            return array
        }

        private fun serializeConfiguration(
            src: ConfigurationSerializable, typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            val map: MutableMap<String, Any> = LinkedHashMap()
            val typeToken = ConfigurationSerialization.getAlias(src.javaClass)
            map[ConfigurationSerialization.SERIALIZED_TYPE_KEY] = typeToken
            map.putAll(src.serialize())
            return context.serialize(map, MutableMap::class.java)
        }

        private fun deserializeMap(
            json: JsonElement, typeOfT: Type,
            context: JsonDeserializationContext
        ): Map<String, Any?> {
            if (!json.isJsonObject) {
                throw JsonParseException("Expected JsonObject, got " + json::class.java.simpleName)
            }
            val jsonObject: JsonObject = json.asJsonObject
            val map: MutableMap<String, Any?> = LinkedHashMap(jsonObject.entrySet().size, 1.0f)
            for ((key, value) in jsonObject.entrySet()) {
                map[key] = deserializeValue(value, context)
            }
            return map
        }

        private fun deserializeArray(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): List<Any?> {
            if (!json.isJsonArray) {
                throw JsonParseException("Expected JsonArray, got " + json::class.java.simpleName)
            }
            val array: JsonArray = json.asJsonArray
            val list: MutableList<Any?> = ArrayList<Any?>(array.size())
            for (element in array) {
                list.add(deserializeValue(element, context))
            }
            return list
        }

        private fun deserializeConfiguration(
            json: JsonElement, typeOfT: Type,
            context: JsonDeserializationContext
        ): ConfigurationSerializable {
            if (!json.isJsonObject) {
                throw JsonParseException("Expected JsonObject, got " + json::class.java.simpleName)
            }
            val map: Map<String, Any?> = context.deserialize(json, MutableMap::class.java)
            return try {
                ConfigurationSerialization.deserializeObject(map)
            } catch (e: Exception) {
                throw JsonParseException(e)
            }
        }

        private fun serializeValue(`object`: Any, context: JsonSerializationContext): JsonElement {
            if (`object` is MemorySection) {
                val section = `object`
                val map: Map<String, Any> = section.getKeys(false).stream().collect(toMap({ key -> key }, section::get))
                return context.serialize(map, MutableMap::class.java)
            } else if (`object` is ConfigurationSerializable) {
                return context.serialize(`object`, ConfigurationSerializable::class.java)
            } else if (`object` is List<*>) {
                return context.serialize(`object`, MutableList::class.java)
            }
            return context.serialize(`object`)
        }

        private fun deserializeValue(element: JsonElement, context: JsonDeserializationContext): Any? {
            if (element.isJsonObject) {
                val `object`: JsonObject = element.asJsonObject
                return if (`object`.has(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                    context.deserialize(element, ConfigurationSerializable::class.java)
                } else {
                    context.deserialize(element, MutableMap::class.java)
                }
            } else if (element.isJsonArray) {
                return context.deserialize(element, MutableList::class.java)
            } else if (element.isJsonPrimitive) {
                val primitive: JsonPrimitive = element.asJsonPrimitive
                if (primitive.isString) {
                    return primitive.asString
                } else if (primitive.isBoolean) {
                    return primitive.asBoolean
                } else if (primitive.isNumber) {
                    val numberString = primitive.asString
                    if (numberString.contains(".")) {
                        return primitive.asDouble
                    }
                    val number = primitive.asInt
                    return if (numberString == number.toString()) {
                        primitive.asInt
                    } else primitive.asLong
                }
                throw JsonParseException("Unknown json primitive: " + primitive.javaClass.simpleName)
            } else if (element.isJsonNull) {
                return null
            }
            throw JsonParseException("Unknown json type: " + element::class.java.simpleName)
        }

        init {
            GSON = GsonBuilder() /* Serializers */
                .registerTypeHierarchyAdapter(MutableMap::class.java,
                    JsonSerializer { src: Map<String, Any>, typeOfSrc: Type, context: JsonSerializationContext ->
                        serializeMap(
                            src,
                            typeOfSrc,
                            context
                        )
                    } as JsonSerializer<Map<String, Any>>)
                .registerTypeHierarchyAdapter(MutableList::class.java,
                    JsonSerializer { src: List<Any>, srcOfType: Type, context: JsonSerializationContext ->
                        serializeArray(
                            src,
                            srcOfType,
                            context
                        )
                    } as JsonSerializer<List<Any>>)
                .registerTypeHierarchyAdapter(ConfigurationSerializable::class.java,
                    JsonSerializer { src: ConfigurationSerializable, typeOfSrc: Type, context: JsonSerializationContext ->
                        serializeConfiguration(
                            src,
                            typeOfSrc,
                            context
                        )
                    } as JsonSerializer<ConfigurationSerializable>) /* Deserializers */
                .registerTypeHierarchyAdapter(MutableMap::class.java,
                    JsonDeserializer { json: JsonElement, typeOfT: Type, context: JsonDeserializationContext ->
                        deserializeMap(
                            json,
                            typeOfT,
                            context
                        )
                    } as JsonDeserializer<Map<String, Any?>>)
                .registerTypeHierarchyAdapter(MutableList::class.java,
                    JsonDeserializer { json: JsonElement, typeOfT: Type, context: JsonDeserializationContext ->
                        deserializeArray(
                            json,
                            typeOfT,
                            context
                        )
                    } as JsonDeserializer<List<Any?>>)
                .registerTypeHierarchyAdapter(ConfigurationSerializable::class.java,
                    JsonDeserializer { json: JsonElement, typeOfT: Type, context: JsonDeserializationContext ->
                        deserializeConfiguration(
                            json,
                            typeOfT,
                            context
                        )
                    } as JsonDeserializer<ConfigurationSerializable>)
                .setPrettyPrinting().serializeNulls().create()
        }
    }

    override fun buildHeader(): String {
        return ""
    }

    @Throws(InvalidConfigurationException::class)
    override fun loadFromString(contents: String) {
        val map: Map<*, *>
        map = try {
            GSON!!.fromJson<Map<*, *>>(contents, MutableMap::class.java)
        } catch (e: Exception) {
            throw InvalidConfigurationException("Failed to parse the content", e)
        }
        for ((key1, value1) in map) {
            val key = key1.toString()
            val value = value1!!
            if (value is Map<*, *>) {
                this.createSection(key, value)
            } else {
                this[key] = value
            }
        }
    }

    override fun saveToString(): String {
        return GSON!!.toJson(map)
    }
}