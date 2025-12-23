package data.atlassian.adf

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Extension block node.
 * Represents an extension with optional marks.
 */
@Serializable
@SerialName("extension")
data class ExtensionNode(
    val attrs: ExtensionAttrs,
    val marks: List<ADFMark>? = null,
) : ADFBlockNode

@Serializable
data class ExtensionAttrs(
    // minLength: 1
    val extensionKey: String,
    // minLength: 1
    val extensionType: String,
    val parameters: JsonObject? = null,
    val text: String? = null,
    val layout: ExtensionLayout? = null,
    // minLength: 1
    val localId: String? = null,
)

@Serializable
enum class ExtensionLayout {
    @SerialName("wide")
    WIDE,

    @SerialName("full-width")
    FULL_WIDTH,

    @SerialName("default")
    DEFAULT,
}

/**
 * Bodied extension block node.
 * Represents an extension with body content.
 */
@Serializable
@SerialName("bodiedExtension")
data class BodiedExtensionNode(
    val attrs: ExtensionAttrs,
    val content: List<ADFBlockNode>,
    val marks: List<ADFMark>? = null,
) : ADFBlockNode

/**
 * Layout section block node.
 * Represents a layout section with columns.
 */
@Serializable
@SerialName("layoutSection")
data class LayoutSectionNode(
    val content: List<LayoutColumnNode>,
    val attrs: LayoutSectionAttrs? = null,
    val marks: List<ADFMark>? = null,
) : ADFBlockNode

@Serializable
data class LayoutSectionAttrs(
    val localId: String? = null,
)

/**
 * Layout column block node.
 * Represents a column within a layout section.
 */
@Serializable
@SerialName("layoutColumn")
data class LayoutColumnNode(
    val attrs: LayoutColumnAttrs,
    val content: List<ADFBlockNode>,
) : ADFBlockNode

@Serializable
data class LayoutColumnAttrs(
    // 0-100
    val width: Double,
    val localId: String? = null,
)
