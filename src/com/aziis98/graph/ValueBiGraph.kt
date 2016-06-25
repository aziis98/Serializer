package com.aziis98.graph

import java.util.*

// Copyright 2016 Antonio De Lucreziis

class ValueBiGraph<V>(value: V) : BiGraph<Int, V>(genUUID(), value) {

    companion object UUID {
        var index = 1

        fun genUUID() = index++
    }

    fun add(value: V) = add(ValueBiGraph(value))

    fun remove(id: Int) = remove(this[id] ?: throw NoSuchElementException())

}