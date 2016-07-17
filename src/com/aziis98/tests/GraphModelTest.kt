package com.aziis98.tests

import com.aziis98.graph.*
import org.junit.Test

// Copyright 2016 Antonio De Lucreziis

internal class GraphModelTest {

    @Test
    fun testGraphModel() {

        val model = GraphModelNodes<String>()

        val node1 = model.addNode("Elemento 1")
        val node2 = model.addNode("Elemento 2")
        val node3 = model.addNode("Elemento 3")

        node1 linkTo node2 linkTo node3 linkTo node1

        model.links.forEach { println(it) }

    }

}