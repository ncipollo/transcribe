# Transcribe

Transcribe is a library for programmatically interacting with Confluence. It converts between [Atlassian Document Format](https://developer.atlassian.com/cloud/jira/platform/apis/document/structure/) and [Markdown](https://github.github.com/gfm/).

Supported use cases:
- Fetch a Confluence page and convert it to Markdown.
- Update a Confluence page with Markdown.
- Update a Confluence template with Markdown.

# Basic Setup

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