package transcribe

import kotlin.reflect.KClass

/**
 * Interface for builders that can create transcriber maps.
 */
interface TranscriberMapBuildable<N : Any, T : Transcriber<N, *>> {
    /**
     * Build and return an immutable map of transcribers.
     */
    fun toMap(): Map<KClass<out N>, T>
}
