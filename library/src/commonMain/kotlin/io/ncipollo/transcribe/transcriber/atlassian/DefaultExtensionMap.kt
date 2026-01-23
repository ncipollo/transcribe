package io.ncipollo.transcribe.transcriber.atlassian

/**
 * Creates a default mapper of extension types to their corresponding transcriber factories.
 * This mapper is initially empty and ready for future extension type registrations.
 */
fun defaultExtensionMapper(): ExtensionMapper {
    return extensionMapper {
        add("com.atlassian.confluence.macro.core:drawio") { DrawioExtensionTranscriber() }
    }
}
