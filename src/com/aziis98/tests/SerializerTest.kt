package com.aziis98.tests

import com.aziis98.graph.*
import com.aziis98.serializer.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.*
import java.nio.file.Paths

// Copyright 2016 Antonio De Lucreziis

internal class SerializerTest {

    // @Test
    fun testIOPerson() {

        val person = Person("Claudia Di Mira", 16)

        Serializer.save(person, Paths.get("res/person2.bdb"), Person.serializer)

        // ---------------- //

        val loaded = Serializer.load(Paths.get("res/person2.bdb"), Person.deserializer)

        assertEquals(person.name, loaded.name)
        assertEquals(person.age, loaded.age)
    }

    class Person(val name: String, val age: Int) {
        companion object {
            val deserializer: InputStream.() -> Person
                get() = {
                    Person(readString(), readInt())
                }

            val serializer: OutputStream.(Person) -> Unit
                get() = { person ->
                    writeString(person.name)
                    writeInt(person.age)
                }
        }
    }

    // ---------------- //

    @Test
    fun testIOCollections() {

        val names = listOf(
            "Antonio" to "De Lucreziis",
            "Claudia" to "Di Mira",
            "Andrea" to "Tommasi",
            "Raffaele" to "Ciruolo"
        )

        Serializer.save(names, Paths.get("res/names.bdb")) { collection ->
            writeCollection(collection) { element ->
                writeString(element.first)
                writeString(element.second)
            }
        }

        // ---------------- //

        val loaded = Serializer.load(Paths.get("res/names.bdb")) {
            readCollection({
                readString() to readString()
            })
        }

        assertEquals(names, loaded)

    }

    @Test
    fun testIOMap() {

        val dictionary = mapOf(
            "casa" to "house",
            "letto" to "bed",
            "tavolo" to "table",
            "essere" to "be",
            "testo" to "text"
        )

        Serializer.save(dictionary, Paths.get("res/dictionary.bdb")) { map ->
            writeMap(map, {
                writeString(it, typeSize = ArraySize.Byte)
            }, {
                writeString(it, typeSize = ArraySize.Byte)
            })
        }

        // ---------------- //

        val loaded = Serializer.load(Paths.get("res/dictionary.bdb")) {
            readMap({
                readString(typeSize = ArraySize.Byte)
            }, {
                readString(typeSize = ArraySize.Byte)
            })
        }

        assertEquals(dictionary, loaded)

    }

    @Test
    fun testIOGraphModel() {

        val path = "res/seq-graph.bdb"

        val outModel = GraphModelNodes<String>()

        val node1 = outModel.addNode("Elemento 1")
        val node2 = outModel.addNode("Elemento 2")
        val node3 = outModel.addNode("Elemento 3")

        node1 linkTo node2 linkTo node3 linkTo node1
        Serializer.save(outModel, Paths.get(path)) { model ->
            writeGraphModel(model) { writeString(it) }
        }

        // ---------------- //

        val inModel = Serializer.load(Paths.get(path)) {
            readGraphModel {
                readString()
            }
        }

        println(inModel.toString())

    }

}