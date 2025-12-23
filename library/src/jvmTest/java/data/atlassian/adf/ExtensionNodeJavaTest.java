package data.atlassian.adf;

import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExtensionNodeJavaTest {
    @Test
    public void testExtensionNodeParametersAccess() {
        // Create JsonObject with sample parameters
        String parametersJson = "{\"color\": \"blue\", \"count\": 42}";
        JsonElement jsonElement = Json.Default.parseToJsonElement(parametersJson);
        JsonObject parameters = (JsonObject) jsonElement;
        
        // Construct ExtensionNode with parameters
        ExtensionAttrs attrs = new ExtensionAttrs(
            "test-key",
            "test-type",
            parameters,
            null,
            null,
            null
        );
        ExtensionNode node = new ExtensionNode(attrs, null);
        
        // Extract a parameter from the JsonObject
        @Nullable JsonObject extractedParams = node.getAttrs().getParameters();
        assertNotNull("Parameters should not be null", extractedParams);
        
        // Extract and assert the "color" parameter
        JsonElement colorElement = extractedParams.get("color");
        assertNotNull("Color parameter should exist", colorElement);
        JsonPrimitive colorPrimitive = (JsonPrimitive) colorElement;
        String color = colorPrimitive.getContent();
        assertEquals("blue", color);
        
        // Extract and assert the "count" parameter
        JsonElement countElement = extractedParams.get("count");
        assertNotNull("Count parameter should exist", countElement);
        JsonPrimitive countPrimitive = (JsonPrimitive) countElement;
        int count = Integer.parseInt(countPrimitive.getContent());
        assertEquals(42, count);
    }
}

