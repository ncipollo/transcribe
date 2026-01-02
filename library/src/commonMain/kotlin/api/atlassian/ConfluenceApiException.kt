package api.atlassian

/**
 * Exception thrown when a Confluence API request fails.
 *
 * @param statusCode The HTTP status code returned by the API
 * @param errorBody The error response body from the API
 * @param message A descriptive error message
 */
class ConfluenceApiException(
    val statusCode: Int,
    val errorBody: String,
    message: String,
) : Exception(message)

