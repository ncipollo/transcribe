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
 * Hard break inline node.
 * Represents a line break.
 */
@Serializable
@SerialName("hardBreak")
data class HardBreakNode(
    val attrs: HardBreakAttrs? = null
) : ADFInlineNode

@Serializable
data class HardBreakAttrs(
    val text: String? = null, // Should be "\n" if present
    val localId: String? = null
)

/**
 * Mention inline node.
 * Represents a user mention.
 */
@Serializable
@SerialName("mention")
data class MentionNode(
    val attrs: MentionAttrs
) : ADFInlineNode

@Serializable
data class MentionAttrs(
    val id: String,
    val text: String? = null,
    val accessLevel: String? = null,
    val userType: MentionUserType? = null,
    val localId: String? = null
)

@Serializable
enum class MentionUserType {
    @SerialName("DEFAULT")
    DEFAULT,
    
    @SerialName("SPECIAL")
    SPECIAL,
    
    @SerialName("APP")
    APP
}

/**
 * Emoji inline node.
 * Represents an emoji.
 */
@Serializable
@SerialName("emoji")
data class EmojiNode(
    val attrs: EmojiAttrs
) : ADFInlineNode

@Serializable
data class EmojiAttrs(
    val shortName: String,
    val id: String? = null,
    val text: String? = null,
    val localId: String? = null
)

/**
 * Date inline node.
 * Represents a date.
 */
@Serializable
@SerialName("date")
data class DateNode(
    val attrs: DateAttrs
) : ADFInlineNode

@Serializable
data class DateAttrs(
    val timestamp: String, // minLength: 1
    val localId: String? = null
)

/**
 * Status inline node.
 * Represents a status badge.
 */
@Serializable
@SerialName("status")
data class StatusNode(
    val attrs: StatusAttrs
) : ADFInlineNode

@Serializable
data class StatusAttrs(
    val text: String, // minLength: 1
    val color: StatusColor,
    val localId: String? = null,
    val style: String? = null
)

@Serializable
enum class StatusColor {
    @SerialName("neutral")
    NEUTRAL,
    
    @SerialName("purple")
    PURPLE,
    
    @SerialName("blue")
    BLUE,
    
    @SerialName("red")
    RED,
    
    @SerialName("yellow")
    YELLOW,
    
    @SerialName("green")
    GREEN
}

/**
 * Inline card node.
 * Represents an inline card with url or data.
 * Schema uses anyOf: either url is required, or data is required.
 */
@Serializable
@SerialName("inlineCard")
data class InlineCardNode(
    val attrs: InlineCardAttrs
) : ADFInlineNode

@Serializable
data class InlineCardAttrs(
    val url: String? = null,
    val data: JsonObject? = null,
    val localId: String? = null
)

/**
 * Placeholder inline node.
 * Represents a placeholder.
 */
@Serializable
@SerialName("placeholder")
data class PlaceholderNode(
    val attrs: PlaceholderAttrs
) : ADFInlineNode

@Serializable
data class PlaceholderAttrs(
    val text: String,
    val localId: String? = null
)

/**
 * Media inline node.
 * Represents inline media.
 */
@Serializable
@SerialName("mediaInline")
data class MediaInlineNode(
    val attrs: MediaInlineAttrs,
    val marks: List<ADFMark>? = null
) : ADFInlineNode

@Serializable
data class MediaInlineAttrs(
    val id: String, // minLength: 1
    val collection: String,
    val type: MediaInlineType,
    val localId: String? = null,
    val alt: String? = null,
    val width: Double? = null,
    val height: Double? = null,
    val occurrenceKey: String? = null, // minLength: 1
    val data: JsonObject? = null
)

@Serializable
enum class MediaInlineType {
    @SerialName("link")
    LINK,
    
    @SerialName("file")
    FILE,
    
    @SerialName("image")
    IMAGE
}

/**
 * Inline extension node.
 * Represents an inline extension.
 */
@Serializable
@SerialName("inlineExtension")
data class InlineExtensionNode(
    val attrs: InlineExtensionAttrs,
    val marks: List<ADFMark>? = null
) : ADFInlineNode

@Serializable
data class InlineExtensionAttrs(
    val extensionKey: String, // minLength: 1
    val extensionType: String, // minLength: 1
    val parameters: JsonObject? = null,
    val text: String? = null,
    val localId: String? = null // minLength: 1
)

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

