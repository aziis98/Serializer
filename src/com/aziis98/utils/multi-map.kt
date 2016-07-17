package com.aziis98.utils

// Copyright 2016 Antonio De Lucreziis

class MultiMap<K, V>() {

    private val map = mutableMapOf<K, MutableList<V>>()

    fun clear() {
        map.clear()
    }

    fun put(key: K, value: V): Boolean {
        checkValueList(key)

        return map[key]!!.add(value)
    }

    fun putList(key: K, values: Collection<V>) {
        values.forEach {
            put(key, it)
        }
    }

    fun putValues(key: K, vararg values: V) {
        values.forEach {
            put(key, it)
        }
    }

    fun putAll(from: Map<out K, V>) {
        from.forEach { entry ->
            put(entry.key, entry.value)
        }
    }

    fun removeAllByKey(key: K): MutableList<V>? {
        return map.remove(key)
    }

    val size: Int
        get() = map.size

    val deepSize: Int
        get() = map.values.map { it.size }.sum()

    fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

    fun containsValue(value: V): Boolean {
        return map.any { entry -> entry.value.contains(value) }
    }

    fun get(key: K): MutableList<V>? {
        return map[key]
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun toString(): String {
        return map.toString()
    }

    private fun checkValueList(key: K) {
        if (!map.containsKey(key)) {
            map.put(key, mutableListOf())
        }
    }

}

/**
 * Ex. multiMapOf(1 to 'A', 2 to 'B', 3 to 'C', 2 to 'X')
 */
fun <K, V> multiMapOf(vararg pairs: Pair<K, V>) = MultiMap<K, V>().apply {
    pairs.forEach {
        put(it.first, it.second)
    }
}

/**
 * Ex. multiMapOfLists(1 to listOf('A'), 2 to listOf('B', 'X'))
 */
fun <K, V> multiMapOfLists(vararg pairs: Pair<K, Collection<V>>) = MultiMap<K, V>().apply {
    pairs.forEach {
        putList(it.first, it.second)
    }
}