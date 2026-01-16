package fetch

import api.atlassian.Attachment
import api.atlassian.FooterComment
import api.atlassian.InlineComment
import api.atlassian.PageResponse

internal data class PageFetchResult(
    val page: PageResponse,
    val attachments: List<Attachment>,
    val footerComments: List<FooterComment>,
    val inlineComments: List<InlineComment>,
)
