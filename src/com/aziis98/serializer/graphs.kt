package com.aziis98.serializer

import com.aziis98.graph.*
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

fun <T, L> OutputStream.writeGraphModel(graphModel: GraphModel<T, L>,
                                   valueSerializer: OutputStream.(T) -> Unit,
                                   arrowDataSerializer: OutputStream.(L?) -> Unit) {
    writeMap(graphModel.nodes, OutputStream::writeInt) {
        valueSerializer(it.value)
    }
    writeCollection(graphModel.links) {
        writeInt(it.start.id)
        arrowDataSerializer(it.data)
        writeInt(it.end.id)
    }
}

fun <T> OutputStream.writeGraphModel(graphModel: GraphModel<T, Nothing>,
                                     valueSerializer: OutputStream.(T) -> Unit) {
    writeGraphModel(graphModel, valueSerializer, { })
}

fun <T, L> InputStream.readGraphModel(valueDeserializer: InputStream.() -> T,
                                      arrowDataDeserializer: InputStream.() -> L?): GraphModel<T, L> {
    val graphModel = GraphModel<T, L>()

    readMap(InputStream::readInt, valueDeserializer).forEach { entry ->
        graphModel.addNode(id = entry.key, value = entry.value)
    }

    readCollection({
        graphModel.addLinkById(readInt(), readInt()).apply {
            data = arrowDataDeserializer()
        }
    })

    return graphModel
}

fun <T> InputStream.readGraphModel(valueDeserializer: InputStream.() -> T): GraphModel<T, Nothing> {
    return readGraphModel(valueDeserializer, { null })
}