package io.ncipollo.transcribe.api.atlassian

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

object ConfluenceHttpClientFactory {
    @OptIn(ExperimentalEncodingApi::class)
    @JvmStatic
    @JvmOverloads
    fun create(
        siteName: String,
        authMaterial: AtlassianAuthMaterial,
        apiVersion: String = "v2",
        logging: Boolean = false,
        logLevel: LogLevel = LogLevel.INFO,
        httpClientEngine: HttpClientEngine? = null,
    ): HttpClient {
        val baseUrl =
            when (apiVersion) {
                "v1" -> "https://$siteName.atlassian.net/wiki/rest/api/"
                "v2" -> "https://$siteName.atlassian.net/wiki/api/v2/"
                else -> throw IllegalArgumentException("Unsupported API version: $apiVersion. Supported versions: v1, v2")
            }
        val credentials = "${authMaterial.email}:${authMaterial.apiToken}"
        val authHeader = "Basic ${Base64.encode(credentials.encodeToByteArray())}"

        val clientConfig: HttpClientConfig<*>.() -> Unit = {
            install(DefaultRequest) {
                url(baseUrl)
                header(HttpHeaders.Authorization, authHeader)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                        explicitNulls = false
                    },
                )
            }

            if (logging) {
                install(Logging) {
                    this.level = logLevel
                }
            }
        }

        return if (httpClientEngine != null) {
            HttpClient(httpClientEngine, clientConfig)
        } else {
            HttpClient(clientConfig)
        }
    }
}
