package com.aziis98.serializer

import com.aziis98.graph.Graph
import java.io.*
import java.util.*

// Copyright 2016 Antonio De Lucreziis

fun <K, V> OutputStream.writeGraph(graph: Graph<K, V>,
                                   keySerializer: OutputStream.(K) -> Unit,
                                   valueSerializer: OutputStream.(V?) -> Unit) {
    valueSerializer(graph.value)
    writeMap(graph.children, keySerializer) {
        writeGraph(it, keySerializer, valueSerializer)
    }
}

fun <K, V> InputStream.readGraph(keyDeserializer: InputStream.() -> K,
                                 valueDeserializer: InputStream.() -> V): Graph<K, V> {
    val graph = Graph<K, V>(valueDeserializer())

    graph.children = HashMap(
        readMap(keyDeserializer) {
            readGraph(keyDeserializer, valueDeserializer)
        }
    )

    return graph
}