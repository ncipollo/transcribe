package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.DocNode
import org.intellij.markdown.ast.ASTNode
import transcribe.TranscribeResult

class MarkdownDocumentTranscriber(
    private val mapper: MarkdownNodeMapper,
) : MarkdownTranscriber<DocNode> {
    override fun transcribe(
        input: ASTNode,
        context: MarkdownContext,
    ): TranscribeResult<DocNode> {
        val blockNodes = mapper.transcribeBlockChildren(input, context)
        return TranscribeResult(DocNode(version = 1, content = blockNodes))
    }
}
