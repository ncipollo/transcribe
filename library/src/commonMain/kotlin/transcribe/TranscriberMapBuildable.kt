package transcribe

import kotlin.reflect.KClass

/**
 * Interface for builders that can create transcriber maps.
 */
interface TranscriberMapBuildable<N : Any, T : Transcriber<out N, *>> {
    /**
     * Build and return an immutable map of transcribers.
     */
    fun build(): Map<KClass<out N>, T>
}
