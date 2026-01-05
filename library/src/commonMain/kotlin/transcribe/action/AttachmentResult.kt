package transcribe.action

data class AttachmentResult(
    val data: ByteArray,
    val localRelativePath: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AttachmentResult

        if (!data.contentEquals(other.data)) return false
        if (localRelativePath != other.localRelativePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + localRelativePath.hashCode()
        return result
    }
}

