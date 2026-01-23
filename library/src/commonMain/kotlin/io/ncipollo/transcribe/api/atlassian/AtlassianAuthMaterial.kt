package io.ncipollo.transcribe.api.atlassian

import kotlin.jvm.JvmStatic

data class AtlassianAuthMaterial(val email: String = "", val apiToken: String = "") {
    companion object {
        @JvmStatic
        fun builder(): AtlassianAuthMaterialBuilder = AtlassianAuthMaterialBuilder()
    }
}

/**
 * Builder for creating AtlassianAuthMaterial instances.
 * Provides a fluent API that is ergonomic for JVM callers.
 */
class AtlassianAuthMaterialBuilder {
    private var email: String = ""
    private var apiToken: String = ""

    /**
     * Set the Atlassian account email.
     */
    fun email(email: String): AtlassianAuthMaterialBuilder {
        this.email = email
        return this
    }

    /**
     * Set the Atlassian API token.
     */
    fun apiToken(apiToken: String): AtlassianAuthMaterialBuilder {
        this.apiToken = apiToken
        return this
    }

    /**
     * Build the AtlassianAuthMaterial instance.
     */
    fun build(): AtlassianAuthMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken)
}
