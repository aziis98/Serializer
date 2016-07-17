package com.aziis98.graph

import formatRec
import java.util.*

// Copyright 2016 Antonio De Lucreziis

class Graph<K, V>(val value: V? = null) {

    companion object {
        fun <K, V> createRoot() = Graph<K, V>()
    }

    var children = hashMapOf<K, Graph<K, V>>()

    operator fun get(key: K): Graph<K, V> {
        return children[key] ?: throw NoSuchElementException()
    }

    fun add(key: K) = add(key, null)

    fun add(key: K, value: V?): Graph<K, V> {
        val graphNode = Graph<K, V>(value)
        children.put(key, graphNode)
        return graphNode
    }

    fun remove(key: K) {
        children.remove(key)
    }

    fun forEach(action: (K, Graph<K, V>) -> Unit) {
        children.forEach(action)
    }

    fun forEach(action: (Graph<K, V>) -> Unit) {
        children.values.forEach(action)
    }

    // -------------------- //

    val valueNotNull: V
        get() = value!!

    override fun toString() = "Graph(${value ?: ""})"

    fun toFormattedString(): String {
        return formatRec(this) { graph, rec ->
            appendln("-> $graph")
            indented {
                graph.forEach { key, graph ->
                    appendIndentation()
                    append("$key ")
                    rec(graph)
                }
            }
        }
    }

}