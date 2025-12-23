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
 * Blockquote block node.
 * Contains paragraphs, lists, code blocks, and media.
 */
@Serializable
@SerialName("blockquote")
data class BlockquoteNode(
    val content: List<ADFBlockNode>,
    val attrs: BlockquoteAttrs? = null
) : ADFBlockNode

@Serializable
data class BlockquoteAttrs(
    val localId: String? = null
)

/**
 * Rule block node.
 * Represents a horizontal rule (no content).
 */
@Serializable
@SerialName("rule")
data class RuleNode(
    val attrs: RuleAttrs? = null
) : ADFBlockNode

@Serializable
data class RuleAttrs(
    val localId: String? = null
)

/**
 * Panel block node.
 * Represents an info panel with various types (info, note, tip, warning, error, success, custom).
 */
@Serializable
@SerialName("panel")
data class PanelNode(
    val attrs: PanelAttrs,
    val content: List<ADFBlockNode>
) : ADFBlockNode

@Serializable
data class PanelAttrs(
    val panelType: PanelType,
    val panelIcon: String? = null,
    val panelIconId: String? = null,
    val panelIconText: String? = null,
    val panelColor: String? = null,
    val localId: String? = null
)

@Serializable
enum class PanelType {
    @SerialName("info")
    INFO,
    
    @SerialName("note")
    NOTE,
    
    @SerialName("tip")
    TIP,
    
    @SerialName("warning")
    WARNING,
    
    @SerialName("error")
    ERROR,
    
    @SerialName("success")
    SUCCESS,
    
    @SerialName("custom")
    CUSTOM
}

/**
 * Expand block node.
 * Represents an expandable section with a title.
 * Marks must be empty (maxItems: 0 in schema).
 */
@Serializable
@SerialName("expand")
data class ExpandNode(
    val content: List<ADFBlockNode>,
    val attrs: ExpandAttrs? = null,
    val marks: List<ADFMark> = emptyList() // Must be empty per schema
) : ADFBlockNode

@Serializable
data class ExpandAttrs(
    val title: String? = null,
    val localId: String? = null
)

/**
 * Nested expand block node.
 * Represents a nested expandable section within an expand node.
 */
@Serializable
@SerialName("nestedExpand")
data class NestedExpandNode(
    val attrs: NestedExpandAttrs,
    val content: List<ADFBlockNode>
) : ADFBlockNode

@Serializable
data class NestedExpandAttrs(
    val title: String? = null,
    val localId: String? = null
)

/**
 * Block card node.
 * Represents a block-level card with url, data, or datasource.
 * Schema uses anyOf: either datasource is required, or url is required, or data is required.
 */
@Serializable
@SerialName("blockCard")
data class BlockCardNode(
    val attrs: BlockCardAttrs
) : ADFBlockNode

@Serializable
data class BlockCardAttrs(
    val url: String? = null,
    val data: JsonObject? = null,
    val datasource: BlockCardDatasource? = null,
    val width: Double? = null,
    val layout: CardLayout? = null,
    val localId: String? = null
)

@Serializable
data class BlockCardDatasource(
    val id: String,
    val parameters: JsonObject,
    val views: List<BlockCardView> // minItems: 1
)

@Serializable
data class BlockCardView(
    val type: String,
    val properties: JsonObject? = null
)

@Serializable
enum class CardLayout {
    @SerialName("wide")
    WIDE,
    
    @SerialName("full-width")
    FULL_WIDTH,
    
    @SerialName("center")
    CENTER,
    
    @SerialName("wrap-right")
    WRAP_RIGHT,
    
    @SerialName("wrap-left")
    WRAP_LEFT,
    
    @SerialName("align-end")
    ALIGN_END,
    
    @SerialName("align-start")
    ALIGN_START
}

/**
 * Embed card node.
 * Represents an embedded card.
 */
@Serializable
@SerialName("embedCard")
data class EmbedCardNode(
    val attrs: EmbedCardAttrs
) : ADFBlockNode

@Serializable
data class EmbedCardAttrs(
    val url: String,
    val layout: CardLayout,
    val width: Double? = null, // 0-100
    val originalHeight: Double? = null,
    val originalWidth: Double? = null,
    val localId: String? = null
)

/**
 * Task list block node.
 * Contains task items and block task items.
 */
@Serializable
@SerialName("taskList")
data class TaskListNode(
    val attrs: TaskListAttrs,
    val content: List<ADFNode> // Can be TaskItemNode, BlockTaskItemNode, or nested TaskListNode
) : ADFBlockNode

@Serializable
data class TaskListAttrs(
    val localId: String
)

/**
 * Task item inline node.
 * Represents a task item with inline content.
 */
@Serializable
@SerialName("taskItem")
data class TaskItemNode(
    val attrs: TaskItemAttrs,
    val content: List<ADFInlineNode>? = null
) : ADFInlineNode

@Serializable
data class TaskItemAttrs(
    val localId: String,
    val state: TaskState
)

/**
 * Block task item block node.
 * Represents a task item with block content.
 */
@Serializable
@SerialName("blockTaskItem")
data class BlockTaskItemNode(
    val attrs: TaskItemAttrs,
    val content: List<ADFBlockNode> // Can contain paragraphs or extensions
) : ADFBlockNode

@Serializable
enum class TaskState {
    @SerialName("TODO")
    TODO,
    
    @SerialName("DONE")
    DONE
}

/**
 * Decision list block node.
 * Contains decision items.
 */
@Serializable
@SerialName("decisionList")
data class DecisionListNode(
    val attrs: DecisionListAttrs,
    val content: List<DecisionItemNode>
) : ADFBlockNode

@Serializable
data class DecisionListAttrs(
    val localId: String
)

/**
 * Decision item inline node.
 * Represents a decision item with inline content.
 */
@Serializable
@SerialName("decisionItem")
data class DecisionItemNode(
    val attrs: DecisionItemAttrs,
    val content: List<ADFInlineNode>? = null
) : ADFInlineNode

@Serializable
data class DecisionItemAttrs(
    val localId: String,
    val state: String
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

