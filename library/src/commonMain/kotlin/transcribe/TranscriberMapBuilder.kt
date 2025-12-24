package transcribe

import kotlin.reflect.KClass

/**
 * Builder for creating a map of node types to transcribers.
 */
class TranscriberMapBuilder<N : Any, T : Transcriber<out N, *>> : TranscriberMapBuildable<N, T> {
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
     * Add all transcribers from an existing map.
     */
    fun addAll(transcriberMap: Map<KClass<out N>, T>): TranscriberMapBuilder<N, T> {
        map.putAll(transcriberMap)
        return this
    }

    /**
     * Build immutable map.
     */
    override fun build(): Map<KClass<out N>, T> {
        return map.toMap()
    }
}

/**
 * DSL for building transcriber maps.
 */
inline fun <N : Any, T : Transcriber<out N, *>> transcriberMap(
    block: TranscriberMapBuilder<N, T>.() -> Unit
): Map<KClass<out N>, T> {
    return TranscriberMapBuilder<N, T>().apply(block).build()
}

