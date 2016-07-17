package com.aziis98.tests

import com.aziis98.utils.multiMapOf
import org.junit.Test

// Copyright 2016 Antonio De Lucreziis

class MultiMapTest {

    @Test
    fun testMultiMap() {

        val multiMap = multiMapOf(
            "essere" to listOf("sono", "sei", "è", "siamo", "siete", "sono")
        )

        println(multiMap.toString())

    }

}