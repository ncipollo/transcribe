package io.ncipollo.transcribe.files

/**
 * Converts a string to snake_case format.
 * Handles mixed case, spaces, and camelCase by converting to lowercase with underscores.
 * Preserves file extensions (text after the last dot).
 *
 * Examples:
 * - "Test Image.png" -> "test_image.png"
 * - "testImageFile.png" -> "test_image_file.png"
 * - "TEST IMAGE.PNG" -> "test_image.png"
 */
fun String.toSnakeCase(): String {
    if (isEmpty()) return this

    // Split on the last dot to preserve file extension
    val lastDotIndex = lastIndexOf('.')
    val (base, extension) = if (lastDotIndex >= 0) {
        substring(0, lastDotIndex) to ".${substring(lastDotIndex + 1).lowercase()}"
    } else {
        this to ""
    }

    // Convert camelCase to snake_case by inserting underscore before uppercase letters
    val withUnderscoresBeforeCaps = base.replace(Regex("([a-z])([A-Z])")) {
        "${it.groupValues[1]}_${it.groupValues[2]}"
    }

    // Replace spaces and multiple underscores with single underscore
    val normalized = withUnderscoresBeforeCaps
        .replace(Regex("\\s+"), "_")
        .replace(Regex("_+"), "_")
        .lowercase()

    // Remove leading/trailing underscores
    val trimmed = normalized.trim('_')

    return if (trimmed.isEmpty()) extension else "$trimmed$extension"
}

/**
 * Drops the file extension from a string (everything after the last dot).
 *
 * Examples:
 * - "file.txt" -> "file"
 * - "document.pdf" -> "document"
 * - "no_extension" -> "no_extension"
 */
fun String.dropExtension(): String {
    val lastDotIndex = lastIndexOf('.')
    return if (lastDotIndex >= 0) {
        substring(0, lastDotIndex)
    } else {
        this
    }
}
