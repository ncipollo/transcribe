package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.DocNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.TranscribeResult

class MarkdownDocumentTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<DocNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<DocNode> {
        val blockResult = mapper.transcribeBlockChildren(input, context)
        return TranscribeResult(DocNode(version = 1, content = blockResult.content), blockResult.actions)
    }
}
