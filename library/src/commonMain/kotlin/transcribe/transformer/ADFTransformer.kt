package transcribe.transformer

import data.atlassian.adf.ADFBlockNode

/**
 * Interface for transforming ADF node trees.
 * Transformers allow customization of ADF structures at key points in the transcription pipeline.
 *
 * @param Context The type of context passed to the transformer
 */
interface ADFTransformer<Context> {
    /**
     * Transforms a list of ADF block nodes.
     *
     * @param nodes The list of ADF block nodes to transform
     * @param context Additional context for the transformation
     * @return The transformed list of ADF block nodes
     */
    fun transform(
        nodes: List<ADFBlockNode>,
        context: Context,
    ): List<ADFBlockNode>
}
