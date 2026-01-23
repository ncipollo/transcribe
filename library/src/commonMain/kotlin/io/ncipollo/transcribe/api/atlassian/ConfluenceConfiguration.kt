package io.ncipollo.transcribe.api.atlassian

import kotlin.jvm.JvmStatic

data class ConfluenceConfiguration(val siteName: String = "", val authMaterial: AtlassianAuthMaterial = AtlassianAuthMaterial()) {
    companion object {
        @JvmStatic
        fun builder(): ConfluenceConfigurationBuilder = ConfluenceConfigurationBuilder()
    }
}

/**
 * Builder for creating ConfluenceConfiguration instances.
 * Provides a fluent API that is ergonomic for JVM callers.
 */
class ConfluenceConfigurationBuilder {
    private var siteName: String = ""
    private var authMaterial: AtlassianAuthMaterial = AtlassianAuthMaterial()

    /**
     * Set the Confluence site name.
     */
    fun siteName(siteName: String): ConfluenceConfigurationBuilder {
        this.siteName = siteName
        return this
    }

    /**
     * Set the Atlassian authentication material.
     */
    fun authMaterial(authMaterial: AtlassianAuthMaterial): ConfluenceConfigurationBuilder {
        this.authMaterial = authMaterial
        return this
    }

    /**
     * Build the ConfluenceConfiguration instance.
     */
    fun build(): ConfluenceConfiguration = ConfluenceConfiguration(siteName = siteName, authMaterial = authMaterial)
}
