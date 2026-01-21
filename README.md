# Transcribe

Transcribe is a library for programmatically interacting with Confluence. It converts between [Atlassian Document Format](https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/) and [Markdown](https://github.github.com/gfm/).

Supported use cases:
- Fetch a Confluence page and convert it to Markdown.
- Update a Confluence page with Markdown.
- Update a Confluence template with Markdown.

## Basic Setup

Configure Transcribe with a `TranscribeConfiguration`. You will typically provide:
- **Site Name**: The subdomain from your Confluence URL (e.g., `your-site` from `https://your-site.atlassian.net/wiki/...`)
- **Email**: The email address associated with your Atlassian account
- **API Token**: Your [Atlassian API token](https://support.atlassian.com/atlassian-account/docs/manage-api-tokens-for-your-atlassian-account/)

```kotlin
val configuration = TranscribeConfiguration.builder()
    .confluenceConfiguration(
        ConfluenceConfiguration(
            siteName = "your-site",
            authMaterial = AtlassianAuthMaterial(
                email = "your-email@example.com",
                apiToken = "your-api-token"
            )
        )
    )
    .build()

val transcribe = Transcribe(configuration)
```

## Fetch a Confluence Page

```kotlin
val result = transcribe.getPageMarkdown(pageUrl)
val markdown = result.markdown
val attachments = result.attachmentResults
val comments = result.commentResult
```

This returns a `PageMarkdownResult` containing:
- **markdown**: The page body as Markdown. Image elements reference the original Confluence attachment URLs.
- **attachmentResults**: A list of `AttachmentResult` objects with downloaded image data. These will typically represent visual elements from the confluence page and are translated into image elements in the markdown body.
- **commentResult**: A `CommentResult` object containing inline comments and footer comments from the page, with each comment converted to Markdown format.

Each `AttachmentResult` provides:
- **data**: The raw image bytes
- **localRelativePath**: A suggested relative path for saving locally

Each `CommentResult` provides:
- **inlineComments**: A list of inline comments from the page
- **footerComments**: A list of footer (page-level) comments

Each `Comment` includes:
- **id**: The comment identifier
- **authorId**: The author's Atlassian account ID
- **createdAt**: ISO 8601 timestamp of comment creation
- **content**: The comment content as Markdown
- **commentType**: The type of comment (INLINE or FOOTER)
- **parentCommentId**: Optional ID of the parent comment if this is a reply

This allows you to optionally save images to disk and the recommended path to save them relative to the markdown (the image elements in markdown will reference these relative paths.)

For example, the returned markdown might look like:

```markdown
# My Page

Here is some content with an image:

![screenshot](images/screenshot.png)
```

The corresponding `AttachmentResult` would have `localRelativePath = "images/screenshot.png"` and contain the image bytes in `data`.

## Update a Confluence Page

```kotlin
val markdown = """
# My Page

Updated content goes here.
""".trimIndent()

val updatedPage = transcribe.updatePageMarkdown(
    url = pageUrl,
    markdown = markdown,
    message = "Updated via Transcribe"  // optional version message
)
```

This converts the Markdown to ADF and updates the page. Returns a `PageResponse` with the updated page metadata.

Alternatively, if you already have the page ID, you can use `updatePageMarkdownByPageId`:

```kotlin
val updatedPage = transcribe.updatePageMarkdownByPageId(
    pageId = "123456",
    markdown = markdown,
    message = "Updated via Transcribe"  // optional version message
)
```

## Update a Confluence Template

```kotlin
val markdown = """
# Template Title

Template content goes here.
""".trimIndent()

val updatedTemplate = transcribe.updateTemplateMarkdown(
    templateId = "template-id",
    markdown = markdown,
    name = "My Template"
)
```

This converts the Markdown to ADF and updates the template. Returns a `TemplateResponse` with the updated template metadata.

## Customization

There are two mechanisms for customizing transcription between Confluence and Markdown.

### Transforming ADF Nodes

You can transform ADF nodes at two points in the pipeline using the `ADFTransformer` interface:
- **toMarkdownTransformer**: Applied after fetching ADF from Confluence, before converting to Markdown
- **toConfluenceTransformer**: Applied after converting Markdown to ADF, before sending to Confluence

```kotlin
// Example transformer that filters out specific node types
class FilteringTransformer<Context> : ADFTransformer<Context> {
    override fun transform(
        nodes: List<ADFBlockNode>,
        context: Context
    ): List<ADFBlockNode> {
        return nodes.filter { node ->
            // Filter out table nodes, for example
            node !is TableNode
        }
    }
}

// Apply transformers in configuration
val configuration = TranscribeConfiguration.builder()
    .confluenceConfiguration(confluenceConfig)
    .toMarkdownTransformer(FilteringTransformer())
    .toConfluenceTransformer(FilteringTransformer())
    .build()
```

### Overriding Transcribers

Translation between ADF and Markdown nodes is handled by Transcribers. You can override the default transcriber for any element type in either direction.

**ADF to Markdown** — Use `adfCustomTranscribers` to override how ADF nodes are converted to Markdown:

```kotlin
val customAdfTranscribers = adfNodeMapper {
    // Override the paragraph transcriber
    add<ParagraphNode> { mapper -> CustomParagraphNodeTranscriber(mapper) }
    // Override the heading transcriber
    add<HeadingNode> { mapper -> CustomHeadingNodeTranscriber(mapper) }
}

val configuration = TranscribeConfiguration.builder()
    .confluenceConfiguration(confluenceConfig)
    .adfCustomTranscribers(customAdfTranscribers)
    .build()
```

**Markdown to ADF** — Use `markdownCustomTranscribers` to override how Markdown elements are converted to ADF:

```kotlin
val customMarkdownTranscribers = markdownNodeMapper {
    // Override the paragraph transcriber
    add(MarkdownElementTypes.PARAGRAPH) { mapper -> CustomParagraphTranscriber(mapper) }
    // Override the code fence transcriber
    add(MarkdownElementTypes.CODE_FENCE) { CustomCodeFenceTranscriber() }
}

val configuration = TranscribeConfiguration.builder()
    .confluenceConfiguration(confluenceConfig)
    .markdownCustomTranscribers(customMarkdownTranscribers)
    .build()
```

See [DefaultADFNodeMap.kt](https://github.com/ncipollo/transcribe/blob/main/library/src/commonMain/kotlin/transcribe/atlassian/DefaultADFNodeMap.kt) and [DefaultMarkdownNodeMap.kt](https://github.com/ncipollo/transcribe/blob/main/library/src/commonMain/kotlin/transcribe/markdown/DefaultMarkdownNodeMap.kt) for the full list of default transcriber mappings.