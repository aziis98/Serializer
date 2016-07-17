package com.aziis98.graph

import java.util.*

// Copyright 2016 Antonio De Lucreziis

open class GraphModel<T, L> {
    val nodes = LinkedList<GraphNode<T>>()

    val links = LinkedList<GraphLink<T, L>>()
    val linkToEnd = HashMap<GraphNode<T>, GraphNode<T>>()
    val linkToStart = HashMap<GraphNode<T>, GraphNode<T>>()

    fun addNode(value: T): GraphNode<T> {
        val node = GraphNode(this, value)
        nodes.add(node)
        return node
    }

    fun addLink(start: GraphNode<T>, end: GraphNode<T>): GraphLink<T, L> {
        val link = GraphLink<T, L>(start, end)
        links.add(link)
        linkToEnd.put(start, end)
        linkToStart.put(end, start)
        return link
    }
}

class GraphModelNodes<T> : GraphModel<T, Nothing>()

class GraphNode<T>(val model: GraphModel<T, *>, var value: T?)

data class GraphLink<T, L>(val start: GraphNode<T>, val end: GraphNode<T>, var data: L? = null)

infix fun <T> GraphNode<T>.linkTo(other: GraphNode<T>) : GraphNode<T> {
    model.addLink(this, other)

    return other
}