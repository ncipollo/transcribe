package data.atlassian.adf

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Text inline node.
 * Represents a text content node with optional formatting marks.
 */
@Serializable
@SerialName("text")
data class TextNode(
    val text: String,
    val marks: List<ADFMark>? = null
) : ADFInlineNode

/**
 * Unknown inline node.
 * Captures unrecognized inline nodes with their type and optional attributes.
 * Used as a fallback when deserializing ADF documents containing node types
 * not yet defined in the codebase.
 */
@Serializable(with = UnknownInlineNodeSerializer::class)
data class UnknownInlineNode(
    val type: String,
    val attrs: JsonObject? = null
) : ADFInlineNode

/**
 * Custom serializer for UnknownInlineNode that handles type and attrs.
 */
object UnknownInlineNodeSerializer : KSerializer<UnknownInlineNode> {
    // Use "nodeType" in descriptor to avoid conflict with JSON class discriminator "type"
    // The actual serialization/deserialization handles the proper node types and writes "type" to JSON
    override val descriptor: SerialDescriptor by lazy {
        buildClassSerialDescriptor("UnknownInlineNode") {
            element<String>("nodeType")
            element<JsonObject>("attrs", isOptional = true)
        }
    }

    override fun serialize(encoder: Encoder, value: UnknownInlineNode) {
        require(encoder is JsonEncoder)
        val jsonObject = buildJsonObject {
            put("type", value.type)
            value.attrs?.let { put("attrs", it) }
        }
        encoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): UnknownInlineNode {
        require(decoder is JsonDecoder)
        val jsonObject = decoder.decodeJsonElement().jsonObject

        val type = jsonObject["type"]?.jsonPrimitive?.content ?: "__unknown_inline__"
        val attrs = jsonObject["attrs"]?.jsonObject

        return UnknownInlineNode(type, attrs)
    }
}

