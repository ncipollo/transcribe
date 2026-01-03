package transcribe.markdown

import context.MarkdownContext
import data.atlassian.adf.ADFNode
import org.intellij.markdown.ast.ASTNode
import transcribe.Transcriber

interface MarkdownTranscriber<T : ADFNode> : Transcriber<ASTNode, T, MarkdownContext>
