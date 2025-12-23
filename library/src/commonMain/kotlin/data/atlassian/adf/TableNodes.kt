package data.atlassian.adf

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Table block node.
 * Contains table rows and optional table attributes.
 */
@Serializable
@SerialName("table")
data class TableNode(
    val content: List<TableRowNode>,
    val attrs: TableAttrs? = null,
    val marks: List<ADFMark>? = null
) : ADFBlockNode

@Serializable
data class TableAttrs(
    val layout: TableLayout? = null,
    val width: Double? = null,
    val displayMode: TableDisplayMode? = null,
    val isNumberColumnEnabled: Boolean? = null,
    val localId: String? = null
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
    DEFAULT
}

@Serializable
enum class TableDisplayMode {
    @SerialName("default")
    DEFAULT,
    
    @SerialName("fixed")
    FIXED
}

/**
 * Table row node.
 * Contains table cells or table headers.
 */
@Serializable
@SerialName("tableRow")
data class TableRowNode(
    val content: List<TableCellContent>,
    val attrs: TableRowAttrs? = null
) : ADFBlockNode

@Serializable
data class TableRowAttrs(
    val localId: String? = null
)

/**
 * Sealed interface for table cell content (tableCell or tableHeader).
 */
@Serializable
sealed interface TableCellContent

/**
 * Table cell node.
 * Contains block content and optional cell attributes.
 */
@Serializable
@SerialName("tableCell")
data class TableCellNode(
    val content: List<ADFBlockNode>,
    val attrs: TableCellAttrs? = null
) : TableCellContent, ADFBlockNode

/**
 * Table header node.
 * Same structure as table cell but semantically represents a header.
 */
@Serializable
@SerialName("tableHeader")
data class TableHeaderNode(
    val content: List<ADFBlockNode>,
    val attrs: TableCellAttrs? = null
) : TableCellContent, ADFBlockNode

@Serializable
data class TableCellAttrs(
    val colspan: Int? = null,
    val rowspan: Int? = null,
    val colwidth: List<Int>? = null,
    val background: String? = null,
    val localId: String? = null
)

