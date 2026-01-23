package io.ncipollo.transcribe

import io.ncipollo.transcribe.api.atlassian.ConfluenceConfiguration
import io.ncipollo.transcribe.context.ADFTranscriberContext
import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.transcriber.atlassian.ADFTranscriberMapBuildable
import io.ncipollo.transcribe.transcriber.atlassian.EmptyADFTranscriberMapBuilder
import io.ncipollo.transcribe.transcriber.markdown.EmptyMarkdownTranscriberMapBuilder
import io.ncipollo.transcribe.transcriber.markdown.MarkdownTranscriberMapBuildable
import io.ncipollo.transcribe.transcriber.transformer.ADFTransformer
import io.ncipollo.transcribe.transcriber.transformer.IdentityADFTransformer
import kotlin.jvm.JvmStatic

data class TranscribeConfiguration(
    val confluenceConfiguration: ConfluenceConfiguration = ConfluenceConfiguration(),
    val adfCustomTranscribers: ADFTranscriberMapBuildable = EmptyADFTranscriberMapBuilder(),
    val markdownCustomTranscribers: MarkdownTranscriberMapBuildable = EmptyMarkdownTranscriberMapBuilder(),
    val toMarkdownTransformer: ADFTransformer<ADFTranscriberContext> = IdentityADFTransformer(),
    val toConfluenceTransformer: ADFTransformer<MarkdownContext> = IdentityADFTransformer(),
) {
    companion object {
        @JvmStatic
        fun builder(): TranscribeConfigurationBuilder = TranscribeConfigurationBuilder()
    }
}

/**
 * Builder for creating TranscribeConfiguration instances.
 * Provides a fluent API that is ergonomic for JVM callers.
 */
class TranscribeConfigurationBuilder {
    private var confluenceConfiguration: ConfluenceConfiguration = ConfluenceConfiguration()
    private var adfCustomTranscribers: ADFTranscriberMapBuildable = EmptyADFTranscriberMapBuilder()
    private var markdownCustomTranscribers: MarkdownTranscriberMapBuildable = EmptyMarkdownTranscriberMapBuilder()
    private var toMarkdownTransformer: ADFTransformer<ADFTranscriberContext> = IdentityADFTransformer()
    private var toConfluenceTransformer: ADFTransformer<MarkdownContext> = IdentityADFTransformer()

    /**
     * Set the Confluence configuration.
     */
    fun confluenceConfiguration(confluenceConfiguration: ConfluenceConfiguration): TranscribeConfigurationBuilder {
        this.confluenceConfiguration = confluenceConfiguration
        return this
    }

    /**
     * Set the custom ADF transcriber map builder.
     */
    fun adfCustomTranscribers(adfCustomTranscribers: ADFTranscriberMapBuildable): TranscribeConfigurationBuilder {
        this.adfCustomTranscribers = adfCustomTranscribers
        return this
    }

    /**
     * Set the custom Markdown transcriber map builder.
     */
    fun markdownCustomTranscribers(markdownCustomTranscribers: MarkdownTranscriberMapBuildable): TranscribeConfigurationBuilder {
        this.markdownCustomTranscribers = markdownCustomTranscribers
        return this
    }

    /**
     * Set the transformer to apply when converting from Confluence to Markdown.
     * This transformer is applied after fetching ADF from Confluence, before transcribing to Markdown.
     */
    fun toMarkdownTransformer(toMarkdownTransformer: ADFTransformer<ADFTranscriberContext>): TranscribeConfigurationBuilder {
        this.toMarkdownTransformer = toMarkdownTransformer
        return this
    }

    /**
     * Set the transformer to apply when converting from Markdown to Confluence.
     * This transformer is applied after transcribing Markdown to ADF, before sending to Confluence.
     */
    fun toConfluenceTransformer(toConfluenceTransformer: ADFTransformer<MarkdownContext>): TranscribeConfigurationBuilder {
        this.toConfluenceTransformer = toConfluenceTransformer
        return this
    }

    /**
     * Build the TranscribeConfiguration instance.
     */
    fun build(): TranscribeConfiguration =
        TranscribeConfiguration(
            confluenceConfiguration = confluenceConfiguration,
            adfCustomTranscribers = adfCustomTranscribers,
            markdownCustomTranscribers = markdownCustomTranscribers,
            toMarkdownTransformer = toMarkdownTransformer,
            toConfluenceTransformer = toConfluenceTransformer,
        )
}
