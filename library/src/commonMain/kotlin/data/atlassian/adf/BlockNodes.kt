package data.atlassian.adf

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Root document node.
 * Every ADF document starts with a doc node containing version and content.
 */
@Serializable
@SerialName("doc")
data class DocNode(
    val version: Int = 1,
    val content: List<ADFBlockNode>
) : ADFBlockNode

/**
 * Paragraph block node.
 * Contains inline content and optional formatting marks.
 */
@Serializable
@SerialName("paragraph")
data class ParagraphNode(
    val content: List<ADFInlineNode>? = null,
    val marks: List<ADFMark>? = null,
    val attrs: ParagraphAttrs? = null
) : ADFBlockNode

@Serializable
data class ParagraphAttrs(
    val localId: String? = null
)

/**
 * Heading block node.
 * Represents headings at levels 1-6.
 */
@Serializable
@SerialName("heading")
data class HeadingNode(
    val attrs: HeadingAttrs,
    val content: List<ADFInlineNode>? = null,
    val marks: List<ADFMark>? = null
) : ADFBlockNode

@Serializable
data class HeadingAttrs(
    val level: Int, // 1-6
    val localId: String? = null
)

/**
 * Bullet list block node.
 * Contains a list of list items.
 */
@Serializable
@SerialName("bulletList")
data class BulletListNode(
    val content: List<ListItemNode>,
    val attrs: BulletListAttrs? = null
) : ADFBlockNode

@Serializable
data class BulletListAttrs(
    val localId: String? = null
)

/**
 * Ordered list block node.
 * Contains a list of list items with optional starting order.
 */
@Serializable
@SerialName("orderedList")
data class OrderedListNode(
    val content: List<ListItemNode>,
    val attrs: OrderedListAttrs? = null
) : ADFBlockNode

@Serializable
data class OrderedListAttrs(
    val order: Int? = null, // minimum: 0
    val localId: String? = null
)

/**
 * List item node.
 * Can contain both block and inline nodes.
 */
@Serializable
@SerialName("listItem")
data class ListItemNode(
    val content: List<ADFNode>,
    val attrs: ListItemAttrs? = null
) : ADFBlockNode

@Serializable
data class ListItemAttrs(
    val localId: String? = null
)

/**
 * Code block node.
 * Contains code text with optional language specification.
 * Marks must be empty (maxItems: 0 in schema).
 */
@Serializable
@SerialName("codeBlock")
data class CodeBlockNode(
    val attrs: CodeBlockAttrs? = null,
    val content: List<TextNode>? = null,
    val marks: List<ADFMark> = emptyList() // Must be empty per schema
) : ADFBlockNode

@Serializable
data class CodeBlockAttrs(
    val language: String? = null,
    val uniqueId: String? = null,
    val localId: String? = null
)

/**
 * Unknown block node.
 * Captures unrecognized block nodes with their type, optional attributes, and optional content.
 * Used as a fallback when deserializing ADF documents containing node types
 * not yet defined in the codebase.
 */
@Serializable(with = UnknownBlockNodeSerializer::class)
data class UnknownBlockNode(
    val type: String,
    val attrs: JsonObject? = null,
    val content: List<ADFNode>? = null
) : ADFBlockNode

/**
 * Custom serializer for UnknownBlockNode that handles type, attrs, and content.
 */
object UnknownBlockNodeSerializer : KSerializer<UnknownBlockNode> {
    // Use JsonArray in descriptor to avoid circular dependency with ADFNode.serializer()
    // Use "nodeType" in descriptor to avoid conflict with JSON class discriminator "type"
    // The actual serialization/deserialization handles the proper ADFNode types and writes "type" to JSON
    override val descriptor: SerialDescriptor by lazy {
        buildClassSerialDescriptor("UnknownBlockNode") {
            element<String>("nodeType")
            element<JsonObject>("attrs", isOptional = true)
            element<JsonArray>("content", isOptional = true)
        }
    }

    override fun serialize(encoder: Encoder, value: UnknownBlockNode) {
        require(encoder is JsonEncoder)
        val json = encoder.json
        val jsonObject = buildJsonObject {
            put("type", value.type)
            value.attrs?.let { put("attrs", it) }
            value.content?.let { content ->
                put("content", json.encodeToJsonElement(
                    ListSerializer(ADFNode.serializer()),
                    content
                ))
            }
        }
        encoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): UnknownBlockNode {
        require(decoder is JsonDecoder)
        val json = decoder.json
        val jsonObject = decoder.decodeJsonElement().jsonObject

        val type = jsonObject["type"]?.jsonPrimitive?.content ?: "__unknown_block__"
        val attrs = jsonObject["attrs"]?.jsonObject
        val content = jsonObject["content"]?.jsonArray?.let { contentArray ->
            json.decodeFromJsonElement(
                ListSerializer(ADFNode.serializer()),
                contentArray
            )
        }

        return UnknownBlockNode(type, attrs, content)
    }
}

