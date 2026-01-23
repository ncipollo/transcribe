package io.ncipollo.transcribe.transcriber.atlassian

/**
 * A simple implementation of ADFTranscriberMapBuildable that returns an empty mapper.
 * Useful when no custom transcriber overrides are needed.
 */
class EmptyADFTranscriberMapBuilder : ADFTranscriberMapBuildable {
    override fun build(): ADFNodeMapper = adfNodeMapper { }
}
