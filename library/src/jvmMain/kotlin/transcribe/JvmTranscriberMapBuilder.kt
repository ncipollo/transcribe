package transcribe

import kotlin.reflect.KClass

/**
 * JVM-specific wrapper around TranscriberMapBuilder that provides
 * convenience methods for working with Java Class objects.
 */
class JvmTranscriberMapBuilder<N : Any, T : Transcriber<N, *>> @PublishedApi internal constructor(
    @PublishedApi internal val builder: TranscriberMapBuilder<N, T>
) {
    constructor() : this(TranscriberMapBuilder())
    /**
     * Add transcriber using Java Class (JVM).
     * Converts the Java Class to KClass before delegating to the builder.
     */
    fun add(nodeClass: Class<out N>, transcriber: T): JvmTranscriberMapBuilder<N, T> {
        builder.add(nodeClass.kotlin, transcriber)
        return this
    }

    /**
     * Add transcriber with explicit KClass (Kotlin).
     */
    fun add(nodeClass: KClass<out N>, transcriber: T): JvmTranscriberMapBuilder<N, T> {
        builder.add(nodeClass, transcriber)
        return this
    }

    /**
     * Add transcriber using reified generics (Kotlin).
     */
    inline fun <reified NodeType : N> add(transcriber: T): JvmTranscriberMapBuilder<N, T> {
        builder.add<NodeType>(transcriber)
        return this
    }

    /**
     * Add all transcribers from an existing map.
     */
    fun addAll(transcriberMap: Map<KClass<out N>, T>): JvmTranscriberMapBuilder<N, T> {
        builder.addAll(transcriberMap)
        return this
    }

    /**
     * Build immutable map.
     */
    fun build(): Map<KClass<out N>, T> {
        return builder.build()
    }
}

/**
 * DSL for building transcriber maps on JVM.
 */
inline fun <N : Any, T : Transcriber<N, *>> jvmTranscriberMap(
    block: JvmTranscriberMapBuilder<N, T>.() -> Unit
): Map<KClass<out N>, T> {
    return JvmTranscriberMapBuilder<N, T>().apply(block).build()
}
