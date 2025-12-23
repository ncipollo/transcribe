package data.atlassian.adf

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Table block node.
 * Represents a table with rows, cells, and headers.
 */
@Serializable
@SerialName("table")
data class TableNode(
    val content: List<TableRowNode>,
    val attrs: TableAttrs? = null,
    val marks: List<ADFMark>? = null,
) : ADFBlockNode

@Serializable
data class TableAttrs(
    val layout: TableLayout? = null,
    val width: Double? = null,
    val isNumberColumnEnabled: Boolean? = null,
    val displayMode: TableDisplayMode? = null,
    // minLength: 1
    val localId: String? = null,
)

@Serializable
enum class TableLayout {
    @SerialName("wide")
    WIDE,

    @SerialName("full-width")
    FULL_WIDTH,

    @SerialName("center")
    CENTER,

    @SerialName("align-end")
    ALIGN_END,

    @SerialName("align-start")
    ALIGN_START,

    @SerialName("default")
    DEFAULT,
}

@Serializable
enum class TableDisplayMode {
    @SerialName("default")
    DEFAULT,

    @SerialName("fixed")
    FIXED,
}

/**
 * Table row node.
 * Contains table cells or headers.
 */
@Serializable
@SerialName("tableRow")
data class TableRowNode(
    // Can be TableCellNode or TableHeaderNode
    val content: List<ADFBlockNode>,
    val attrs: TableRowAttrs? = null,
) : ADFBlockNode

@Serializable
data class TableRowAttrs(
    val localId: String? = null,
)

/**
 * Table cell node.
 * Represents a regular table cell.
 */
@Serializable
@SerialName("tableCell")
data class TableCellNode(
    val content: List<ADFBlockNode>,
    val attrs: TableCellAttrs? = null,
) : ADFBlockNode

/**
 * Table header node.
 * Represents a table header cell.
 */
@Serializable
@SerialName("tableHeader")
data class TableHeaderNode(
    val content: List<ADFBlockNode>,
    val attrs: TableCellAttrs? = null,
) : ADFBlockNode

@Serializable
data class TableCellAttrs(
    val colspan: Int? = null,
    val rowspan: Int? = null,
    val colwidth: List<Double>? = null,
    val background: String? = null,
    val localId: String? = null,
)
