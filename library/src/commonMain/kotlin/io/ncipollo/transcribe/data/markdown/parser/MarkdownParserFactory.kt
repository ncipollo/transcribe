package io.ncipollo.transcribe.data.markdown.parser

import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal fun defaultMarkdownParser() = MarkdownParser(GFMFlavourDescriptor())
