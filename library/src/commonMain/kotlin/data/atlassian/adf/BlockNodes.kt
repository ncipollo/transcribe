package data.atlassian.adf

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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

