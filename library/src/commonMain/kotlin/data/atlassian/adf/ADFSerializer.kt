package data.atlassian.adf

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Serializer helper object for Atlassian Document Format (ADF) documents.
 *
 * Provides methods to convert between JSON strings and ADF node trees.
 * Handles unknown node types gracefully by wrapping them in UnknownBlockNode
 * or UnknownInlineNode.
 */
object ADFSerializer {
    private val serializersModule = SerializersModule {
        // Only needed for defaultDeserializer - sealed classes auto-register their subclasses
        polymorphic(ADFBlockNode::class) {
            defaultDeserializer { UnknownBlockNodeSerializer }
        }
        polymorphic(ADFInlineNode::class) {
            defaultDeserializer { UnknownInlineNodeSerializer }
        }
        polymorphic(ADFNode::class) {
            defaultDeserializer { UnknownBlockNodeSerializer }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
        serializersModule = this@ADFSerializer.serializersModule
    }

    /**
     * Deserializes a JSON string into a DocNode.
     *
     * Unknown node types are wrapped in UnknownBlockNode or UnknownInlineNode
     * based on their context.
     *
     * @param jsonString The JSON string representing an ADF document
     * @return The parsed DocNode
     * @throws kotlinx.serialization.SerializationException if the JSON is invalid
     */
    fun fromJson(jsonString: String): DocNode {
        return json.decodeFromString(DocNode.serializer(), jsonString)
    }

    /**
     * Serializes a DocNode into a JSON string.
     *
     * @param doc The DocNode to serialize
     * @return The JSON string representation
     */
    fun toJson(doc: DocNode): String {
        return json.encodeToString(DocNode.serializer(), doc)
    }
}
