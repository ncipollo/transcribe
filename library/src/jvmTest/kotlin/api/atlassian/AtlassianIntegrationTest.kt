package api.atlassian

import Transcribe
import TranscribeConfiguration
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AtlassianIntegrationTest {
    @Test
    fun getPageMarkdown_fetchesAndTranscribesPage() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val pageUrl = System.getenv("ATLASSIAN_TEST_PAGE_URL")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || pageUrl == null) {
                return@runBlocking
            }

            val configuration =
                TranscribeConfiguration(
                    confluenceConfiguration =
                        ConfluenceConfiguration(
                            siteName = siteName,
                            authMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken),
                        ),
                )
            val transcribe = Transcribe(configuration)

            val markdown = transcribe.getPageMarkdown(pageUrl)

            assertNotNull(markdown)
            assertTrue(markdown.isNotEmpty())

            println("-----------")
            println(markdown)

            transcribe.close()
        }

    private val testMarkdown = """
# Section 1
This is normal paragraph text. Nothing interesting here.

# Section 2
The following are bullets:
- Bullet 1
- Bullet 2
- Bullet 3

# Section 2
The following are bullets:
- Bullet 1
- Bullet 2
- Bullet 3

And here we have numeric bullets:
1. The number one.
2. The number two

And here's a task list:
- [ ] Action 1
- [ ] Action 2
- [x] Checked

# Section 3
| **Column 1** | **Column 2** | **Column 3** |
| --- | --- | --- |
| Row 1, Column 1 | Row 1, Column 2 | Row 1, Column 3 |
| Row 2, Column 1 | Row 2, Column 2 | Row 2, Column 3 |

# Section 5
This will have subsections.

## Sub 1

### H3 Section
Wow, I'm nested!

# Sub 2
Subsection 2.

# Section 6
**Lots** of *random* elements.

```swift
let thing = MyThing()
use(thing)
```

- 🔵 Status 1
- 🔴 Status 2 text after
    """

    @Test
    fun updatePageMarkdown_updatesPageWithMarkdown() =
        runBlocking {
            val apiToken = System.getenv("ATLASSIAN_API_TOKEN")
            val email = System.getenv("ATLASSIAN_EMAIL")
            val siteName = System.getenv("ATLASSIAN_SITE_NAME")
            val updatePageUrl = System.getenv("ATLASSIAN_UPDATE_TEST_PAGE_URL")

            // Skip test if credentials are not available
            if (apiToken == null || email == null || siteName == null || updatePageUrl == null) {
                return@runBlocking
            }

            val configuration =
                TranscribeConfiguration(
                    confluenceConfiguration =
                        ConfluenceConfiguration(
                            siteName = siteName,
                            authMaterial = AtlassianAuthMaterial(email = email, apiToken = apiToken),
                        ),
                )
            val transcribe = Transcribe(configuration)

            val updatedPage =
                transcribe.updatePageMarkdown(
                    url = updatePageUrl,
                    markdown = testMarkdown,
                    message = "Integration test update",
                )

            assertNotNull(updatedPage)
            assertNotNull(updatedPage.id)
            assertNotNull(updatedPage.title)
            assertTrue(updatedPage.title.isNotEmpty())

            println("-----------")
            println("Updated page ID: ${updatedPage.id}")
            println("Updated page title: ${updatedPage.title}")
            println("Page version: ${updatedPage.version.number}")

            transcribe.close()
        }
}
