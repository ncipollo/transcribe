package transcribe

import kotlin.reflect.KClass

/**
 * JVM-specific wrapper around TranscriberMapBuilder that provides
 * convenience methods for working with Java Class objects.
 */
class JvmTranscriberMapBuilder<N : Any, T : Transcriber<out N, *>>(
    private val builder: TranscriberMapBuilder<N, T> = TranscriberMapBuilder()
) : TranscriberMapBuildable<N, T> {

    /**
     * Add transcriber using Java Class (JVM).
     * Converts the Java Class to KClass before delegating to the builder.
     */
    fun add(nodeClass: Class<out N>, transcriber: T): JvmTranscriberMapBuilder<N, T> {
        builder.add(nodeClass.kotlin, transcriber)
        return this
    }

    /**
     * Build immutable map.
     */
    override fun build(): Map<KClass<out N>, T> {
        return builder.build()
    }
}
