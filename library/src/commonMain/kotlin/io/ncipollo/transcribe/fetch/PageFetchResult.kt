package io.ncipollo.transcribe.fetch

import io.ncipollo.transcribe.api.atlassian.Attachment
import io.ncipollo.transcribe.api.atlassian.FooterComment
import io.ncipollo.transcribe.api.atlassian.InlineComment
import io.ncipollo.transcribe.api.atlassian.PageResponse

internal data class PageFetchResult(
    val page: PageResponse,
    val attachments: List<Attachment>,
    val footerComments: List<FooterComment>,
    val inlineComments: List<InlineComment>,
)
