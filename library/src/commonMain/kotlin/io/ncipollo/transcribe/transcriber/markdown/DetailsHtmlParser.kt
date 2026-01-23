package io.ncipollo.transcribe.transcriber.markdown

/**
 * Parses HTML details/summary elements to extract summary text and inner content.
 */
object DetailsHtmlParser {
    /**
     * Parsed result containing the summary text and inner content markdown.
     */
    data class ParsedDetails(
        val summary: String,
        val content: String,
    )

    /**
     * Parses a details HTML block to extract summary and content.
     * Expects HTML in the format: <details><summary>...</summary>...</details>
     *
     * @param htmlText The raw HTML text containing the details element
     * @return ParsedDetails with summary and content, or null if parsing fails
     */
    fun parse(htmlText: String): ParsedDetails? {
        val trimmed = htmlText.trim()

        // Check if it starts with <details> (case-insensitive)
        if (!trimmed.startsWith("<details", ignoreCase = true)) {
            return null
        }

        // Find the opening <summary> tag
        val summaryStartRegex = Regex("<summary[^>]*>", RegexOption.IGNORE_CASE)
        val summaryStartMatch = summaryStartRegex.find(trimmed) ?: return null
        val summaryStartIndex = summaryStartMatch.range.last + 1

        // Find the closing </summary> tag (case-insensitive)
        val summaryEndRegex = Regex("</summary>", RegexOption.IGNORE_CASE)
        val summaryEndMatch = summaryEndRegex.find(trimmed, summaryStartIndex) ?: return null
        val summaryEndIndex = summaryEndMatch.range.first

        // Extract summary text (content between <summary> and </summary>)
        val summary = trimmed.substring(summaryStartIndex, summaryEndIndex).trim()

        // Find content after </summary> and before </details>
        val contentStartIndex = summaryEndMatch.range.last + 1
        val detailsEndRegex = Regex("</details>", RegexOption.IGNORE_CASE)
        val detailsEndMatch = detailsEndRegex.find(trimmed, contentStartIndex) ?: return null
        val detailsEndIndex = detailsEndMatch.range.first

        // Extract content (between </summary> and </details>)
        val content = trimmed.substring(contentStartIndex, detailsEndIndex).trim()

        return ParsedDetails(
            summary = summary,
            content = content,
        )
    }
}
