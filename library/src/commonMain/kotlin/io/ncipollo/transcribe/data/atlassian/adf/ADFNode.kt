package io.ncipollo.transcribe.data.atlassian.adf

import kotlinx.serialization.Serializable

/**
 * Base sealed interface for all Atlassian Document Format nodes.
 *
 * ADF documents are composed of a hierarchy of nodes. There are two categories:
 * - Block nodes: structural elements (headings, paragraphs, lists, etc.)
 * - Inline nodes: content elements (text, images, etc.)
 */
@Serializable
sealed interface ADFNode

/**
 * Sealed interface for block-level nodes.
 * Block nodes define the structural elements of the document such as headings,
 * paragraphs, lists, tables, and alike.
 */
@Serializable
sealed interface ADFBlockNode : ADFNode

/**
 * Sealed interface for inline-level nodes.
 * Inline nodes contain the document content such as text, images, mentions, etc.
 */
@Serializable
sealed interface ADFInlineNode : ADFNode
