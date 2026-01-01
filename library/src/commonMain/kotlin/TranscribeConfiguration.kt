import api.atlassian.ConfluenceConfiguration
import transcribe.atlassian.ADFTranscriberMapBuildable
import transcribe.atlassian.EmptyADFTranscriberMapBuilder
import transcribe.markdown.EmptyMarkdownTranscriberMapBuilder
import transcribe.markdown.MarkdownTranscriberMapBuildable
import kotlin.jvm.JvmStatic

data class TranscribeConfiguration(
    val confluenceConfiguration: ConfluenceConfiguration = ConfluenceConfiguration(),
    val adfCustomTranscribers: ADFTranscriberMapBuildable = EmptyADFTranscriberMapBuilder(),
    val markdownCustomTranscribers: MarkdownTranscriberMapBuildable = EmptyMarkdownTranscriberMapBuilder(),
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
     * Build the TranscribeConfiguration instance.
     */
    fun build(): TranscribeConfiguration =
        TranscribeConfiguration(
            confluenceConfiguration = confluenceConfiguration,
            adfCustomTranscribers = adfCustomTranscribers,
            markdownCustomTranscribers = markdownCustomTranscribers,
        )
}
