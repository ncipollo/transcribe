package transcribe

import kotlin.reflect.KClass

/**
 * A simple implementation of TranscriberMapBuildable that returns an empty map.
 * Useful when no custom transcriber overrides are needed.
 */
class EmptyTranscriberMapBuilder<N : Any, T : Transcriber<out N, *>> : TranscriberMapBuildable<N, T> {
    override fun build(): Map<KClass<out N>, T> = emptyMap()
}

