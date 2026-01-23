package io.ncipollo.transcribe.transcriber.markdown

import io.ncipollo.transcribe.context.MarkdownContext
import io.ncipollo.transcribe.data.atlassian.adf.ADFNode
import org.intellij.markdown.ast.ASTNode
import io.ncipollo.transcribe.transcriber.Transcriber

interface MarkdownTranscriber<T : ADFNode> : Transcriber<ASTNode, T, MarkdownContext>
