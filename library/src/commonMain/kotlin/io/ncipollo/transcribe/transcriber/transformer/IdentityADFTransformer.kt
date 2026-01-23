package io.ncipollo.transcribe.transcriber.transformer

import io.ncipollo.transcribe.data.atlassian.adf.ADFBlockNode

/**
 * Identity transformer that returns the input unchanged.
 * This is the default transformer used when no custom transformation is needed.
 */
class IdentityADFTransformer<Context> : ADFTransformer<Context> {
    override fun transform(
        nodes: List<ADFBlockNode>,
        context: Context,
    ): List<ADFBlockNode> = nodes
}
