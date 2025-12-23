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
 * Sealed interface for ADF marks.
 * Marks define text decoration or formatting such as bold, italics, links, etc.
 */
@Serializable
sealed interface ADFMark

/**
 * Strong (bold) text mark.
 */
@Serializable
@SerialName("strong")
data object StrongMark : ADFMark

/**
 * Emphasis (italic) text mark.
 */
@Serializable
@SerialName("em")
data object EmMark : ADFMark

/**
 * Code (monospace) text mark.
 */
@Serializable
@SerialName("code")
data object CodeMark : ADFMark

/**
 * Strikethrough text mark.
 */
@Serializable
@SerialName("strike")
data object StrikeMark : ADFMark

/**
 * Underline text mark.
 */
@Serializable
@SerialName("underline")
data object UnderlineMark : ADFMark

/**
 * Link mark with href and optional title.
 */
@Serializable
@SerialName("link")
data class LinkMark(
    val attrs: LinkAttrs
) : ADFMark

@Serializable
data class LinkAttrs(
    val href: String,
    val title: String? = null,
    val id: String? = null,
    val collection: String? = null,
    val occurrenceKey: String? = null
)

/**
 * Text color mark with hex color code.
 * Color must match pattern: ^#[0-9a-fA-F]{6}$
 */
@Serializable
@SerialName("textColor")
data class TextColorMark(
    val attrs: TextColorAttrs
) : ADFMark

@Serializable
data class TextColorAttrs(
    val color: String // Pattern: ^#[0-9a-fA-F]{6}$
)

/**
 * Subscript or superscript mark.
 */
@Serializable
@SerialName("subsup")
data class SubSupMark(
    val attrs: SubSupAttrs
) : ADFMark

@Serializable
data class SubSupAttrs(
    val type: SubSupType
)

@Serializable
enum class SubSupType {
    @SerialName("sub")
    SUB,
    
    @SerialName("sup")
    SUP
}

/**
 * Unknown mark.
 * Captures unrecognized marks with their type and optional attributes.
 * Used as a fallback when deserializing ADF documents containing mark types
 * not yet defined in the codebase.
 */
@Serializable(with = UnknownMarkSerializer::class)
data class UnknownMark(
    val type: String,
    val attrs: JsonObject? = null
) : ADFMark

/**
 * Custom serializer for UnknownMark that handles type and attrs.
 */
object UnknownMarkSerializer : KSerializer<UnknownMark> {
    // Use "nodeType" in descriptor to avoid conflict with JSON class discriminator "type"
    // The actual serialization/deserialization handles the proper mark types and writes "type" to JSON
    override val descriptor: SerialDescriptor by lazy {
        buildClassSerialDescriptor("UnknownMark") {
            element<String>("nodeType")
            element<JsonObject>("attrs", isOptional = true)
        }
    }

    override fun serialize(encoder: Encoder, value: UnknownMark) {
        require(encoder is JsonEncoder)
        val jsonObject = buildJsonObject {
            put("type", value.type)
            value.attrs?.let { put("attrs", it) }
        }
        encoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): UnknownMark {
        require(decoder is JsonDecoder)
        val jsonObject = decoder.decodeJsonElement().jsonObject

        val type = jsonObject["type"]?.jsonPrimitive?.content ?: "__unknown_mark__"
        val attrs = jsonObject["attrs"]?.jsonObject

        return UnknownMark(type, attrs)
    }
}
