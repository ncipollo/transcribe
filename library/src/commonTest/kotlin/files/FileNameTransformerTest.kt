package files

import kotlin.test.Test
import kotlin.test.assertEquals

class FileNameTransformerTest {
    @Test
    fun toSnakeCase_withMixedCaseAndSpaces() {
        val input = "Test Image.png"
        val expected = "test_image.png"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withAlreadySnakeCase() {
        val input = "test_image.png"
        val expected = "test_image.png"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withAllUpperCase() {
        val input = "TEST IMAGE.PNG"
        val expected = "test_image.png"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withMultipleSpaces() {
        val input = "Test  Multiple   Spaces.png"
        val expected = "test_multiple_spaces.png"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withCamelCase() {
        val input = "testImageFile.png"
        val expected = "test_image_file.png"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withEmptyString() {
        val input = ""
        val expected = ""

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }

    @Test
    fun toSnakeCase_withNoExtension() {
        val input = "Test Image"
        val expected = "test_image"

        val result = input.toSnakeCase()

        assertEquals(expected, result)
    }
}
