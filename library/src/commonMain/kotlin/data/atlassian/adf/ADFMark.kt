package data.atlassian.adf

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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

