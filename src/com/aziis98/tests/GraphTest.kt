package com.aziis98.tests

import com.aziis98.graph.*
import com.aziis98.serializer.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Paths

// Copyright 2016 Antonio De Lucreziis

internal class GraphTest {

    fun getTestGraph(): Graph<String, Any> {
        val root = Graph<String, Any>()

        root.add("a", 13)
        root.add("b", "Ciao")
        root.add("node").apply {
            add("test1", "prova1")
            add("test2", "prova2")
        }

        return root
    }

    @Test
    fun testGraph() {

        val root = getTestGraph()

        assertEquals("""-> Graph()
                       |    a -> Graph(13)
                       |    node -> Graph()
                       |        test1 -> Graph(prova1)
                       |        test2 -> Graph(prova2)
                       |    b -> Graph(Ciao)
                       |""".trimMargin(), root.toFormattedString())

        println(root.toGraphModel())

    }

    @Test
    fun testGraphIO() {

        val root = getTestGraph()

        Serializer.save(root, Paths.get("res/graph1.bdb")) {
            writeGraph(it, { writeString(it, ArraySize.Byte) }, { writeObject(it) })
        }

        // ------------ //

        val loaded = Serializer.load(Paths.get("res/graph1.bdb")) {
            readGraph({ readString(ArraySize.Byte) }, { readObject() })
        }

        assertEquals("""-> Graph()
                       |    a -> Graph(13)
                       |    node -> Graph()
                       |        test1 -> Graph(prova1)
                       |        test2 -> Graph(prova2)
                       |    b -> Graph(Ciao)
                       |""".trimMargin(), loaded.toFormattedString())

    }

}