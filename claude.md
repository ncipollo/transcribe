# Transcribe Project Guidelines

## Project Overview

Transcribe is a Kotlin Multiplatform library for programmatically interacting with Confluence. It converts between Atlassian Document Format (ADF) and Markdown, enabling you to:
- Fetch a Confluence page and convert it to Markdown
- Update a Confluence page with Markdown
- Update a Confluence template with Markdown

## Kotlin Multiplatform

This project is a kotlin multi-platform project.

- Prefer to place new code in commonMain and commonTest unless explicitly instructed to place it within a platform-specific target.

## Test Names

- Test names should typically be in the form function_condition

## Test Assertions

- When testing functions that return objects, define a single `expected` object and use one `assertEquals(expected, result)` rather than multiple property-level assertions.
- This leverages data class equality and provides clearer test failure messages.

Example:
```kotlin
@Test
fun fromResponse_mapsFields() {
    val response = SomeResponse(...)

    val result = SomeMapper.fromResponse(response)

    val expected = SomeResult(
        field1 = "value1",
        field2 = "value2",
    )
    assertEquals(expected, result)
}
```
