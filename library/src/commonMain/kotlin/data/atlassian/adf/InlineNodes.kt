package data.atlassian.adf

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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

