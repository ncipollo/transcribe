package transcribe

import kotlin.reflect.KClass

/**
 * Builder for creating a map of node types to transcribers.
 */
class TranscriberMapBuilder<N : Any, T : Transcriber<N, *>> {
    private val map = mutableMapOf<KClass<out N>, T>()

    /**
     * Add transcriber using reified generics (Kotlin).
     */
    inline fun <reified NodeType : N> add(transcriber: T): TranscriberMapBuilder<N, T> {
        return add(NodeType::class, transcriber)
    }

    /**
     * Add transcriber with explicit KClass (Kotlin).
     */
    fun add(nodeClass: KClass<out N>, transcriber: T): TranscriberMapBuilder<N, T> {
        map[nodeClass] = transcriber
        return this
    }

    /**
     * Add transcriber with java.lang.Class (Java).
     */
    fun add(nodeClass: Class<out N>, transcriber: T): TranscriberMapBuilder<N, T> {
        return add(nodeClass.kotlin, transcriber)
    }

    /**
     * Add all transcribers from an existing map.
     */
    fun addAll(transcriberMap: Map<KClass<out N>, T>): TranscriberMapBuilder<N, T> {
        map.putAll(transcriberMap)
        return this
    }

    /**
     * Build immutable map.
     */
    fun build(): Map<KClass<out N>, T> {
        return map.toMap()
    }
}

/**
 * DSL for building transcriber maps.
 */
inline fun <N : Any, T : Transcriber<N, *>> transcriberMap(
    block: TranscriberMapBuilder<N, T>.() -> Unit
): Map<KClass<out N>, T> {
    return TranscriberMapBuilder<N, T>().apply(block).build()
}

