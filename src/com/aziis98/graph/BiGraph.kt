package com.aziis98.graph

import java.util.*

// Copyright 2016 Antonio De Lucreziis

open class BiGraph<K, V>(val key: K, val value: V) {

    val parents = HashMap<K, BiGraph<K, V>>()
    val children = HashMap<K, BiGraph<K, V>>()

    operator fun get(key: K) = children[key]

    fun add(key: K, value: V) = add(BiGraph(key, value))

    fun add(other: BiGraph<K, V>): BiGraph<K, V> {
        children.put(other.key, other)
        other.parents.put(key, this)

        return other
    }

    fun remove(other: BiGraph<K, V>) {
        children.remove(other.key)
        other.parents.remove(key)
    }

    inline fun forEach(action: (BiGraph<K, V>) -> Unit) {
        children.values.forEach(action)
    }

}