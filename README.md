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
```

This returns a `PageMarkdownResult` containing:
- **markdown**: The page body as Markdown. Image elements reference the original Confluence attachment URLs.
- **attachmentResults**: A list of `AttachmentResult` objects with downloaded image data. These will typically represent visual elements from the confluence page and are translated into image elements in the markdown body.

Each `AttachmentResult` provides:
- **data**: The raw image bytes
- **localRelativePath**: A suggested relative path for saving locally

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
