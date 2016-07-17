package com.aziis98.graph

import com.aziis98.utils.MultiMap
import java.util.*

// Copyright 2016 Antonio De Lucreziis

const val GENERATED = -1

open class GraphModel<T, L>(val nodes: MutableMap<Int, GraphNode<T>> = HashMap()) {

    val links
        = LinkedList<GraphLink<T, L>>()

    val linkToEnd
        = MultiMap<GraphNode<T>, GraphNode<T>>()
    val linkToStart
        = MultiMap<GraphNode<T>, GraphNode<T>>()

    fun addNode(value: T, id: Int = GENERATED): GraphNode<T> {
        val node = GraphNode(this, value)

        if (id != -1) node.id = id

        nodes.put(node.id, node)
        return node
    }

    fun addLink(start: GraphNode<T>, end: GraphNode<T>): GraphLink<T, L> {
        val link = GraphLink<T, L>(start, end)
        links.add(link)
        linkToEnd.put(start, end)
        linkToStart.put(end, start)
        return link
    }

    fun addLinkById(start: Int, end: Int): GraphLink<T, L> {
        val a = nodes[start]!!
        val b = nodes[end]!!

        return addLink(a, b)
    }

    var nextID = 0
    fun genNodeID() = nextID++

    override fun toString(): String {
        return "{ nodes= $nodes, links= $links }"
    }
}

data class GraphNode<T>(val model: GraphModel<T, *>, var value: T) {
    var id = model.genNodeID()
        internal set(value) { field = value }

    override fun toString(): String {
        return "GraphNode{ $value }"
    }
}

data class GraphLink<T, L>(val start: GraphNode<T>, val end: GraphNode<T>, var data: L? = null) {
    override fun toString(): String {
        return "{ ${start.id} -{ $data }-> ${end.id} }"
    }
}

infix fun <T> GraphNode<T>.linkTo(other: GraphNode<T>) : GraphNode<T> {
    model.addLink(this, other)

    return other
}


class GraphModelNodes<T> : GraphModel<T, Nothing>()

fun <K, V> Graph<K, V>.toGraphModel(): GraphModel<V?, K> {

    val graphModel = GraphModel<V?, K>()

    fun Graph<K, V>.toGraphNode(): GraphNode<V?> {
        val node = graphModel.addNode(value)

        children.forEach { key, graph ->
            val childNode = graph.toGraphNode()
            graphModel.addLink(node, childNode).apply {
                data = key
            }
        }

        return node
    }

    this.toGraphNode()

    return graphModel
}