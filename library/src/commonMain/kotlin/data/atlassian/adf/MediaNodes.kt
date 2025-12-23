package data.atlassian.adf

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Media single block node.
 * Represents a single media item with optional caption.
 */
@Serializable
@SerialName("mediaSingle")
data class MediaSingleNode(
    val content: List<ADFNode>, // Can be MediaNode or CaptionNode
    val attrs: MediaSingleAttrs? = null,
    val marks: List<ADFMark>? = null
) : ADFBlockNode

@Serializable
data class MediaSingleAttrs(
    val layout: MediaSingleLayout,
    val width: Double? = null,
    val widthType: MediaWidthType? = null,
    val localId: String? = null
)

@Serializable
enum class MediaSingleLayout {
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

@Serializable
enum class MediaWidthType {
    @SerialName("percentage")
    PERCENTAGE,
    
    @SerialName("pixel")
    PIXEL
}

/**
 * Media group block node.
 * Contains multiple media nodes.
 */
@Serializable
@SerialName("mediaGroup")
data class MediaGroupNode(
    val content: List<MediaNode>
) : ADFBlockNode

/**
 * Media block node.
 * Represents a media item (file, link, or external).
 */
@Serializable
@SerialName("media")
data class MediaNode(
    val attrs: MediaAttrs,
    val marks: List<ADFMark>? = null
) : ADFBlockNode

@Serializable
data class MediaAttrs(
    val type: MediaType,
    val id: String? = null, // minLength: 1, required for link/file
    val collection: String? = null, // required for link/file
    val url: String? = null, // required for external
    val localId: String? = null,
    val alt: String? = null,
    val width: Double? = null,
    val height: Double? = null,
    val occurrenceKey: String? = null // minLength: 1
)

@Serializable
enum class MediaType {
    @SerialName("link")
    LINK,
    
    @SerialName("file")
    FILE,
    
    @SerialName("external")
    EXTERNAL
}

/**
 * Caption block node.
 * Represents a caption for media.
 */
@Serializable
@SerialName("caption")
data class CaptionNode(
    val content: List<ADFInlineNode>? = null,
    val attrs: CaptionAttrs? = null
) : ADFBlockNode

@Serializable
data class CaptionAttrs(
    val localId: String? = null
)

