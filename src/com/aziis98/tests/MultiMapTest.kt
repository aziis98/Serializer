package com.aziis98.tests

import com.aziis98.utils.multiMapOfLists
import org.junit.Assert.*
import org.junit.Test

// Copyright 2016 Antonio De Lucreziis

class MultiMapTest {

    @Test
    fun testMultiMap() {

        val multiMap = multiMapOfLists(
            "essere" to listOf("sono", "sei", "è", "siamo", "siete", "sono"),
            "avere" to listOf("ho", "hai", "ha", "abbiamo", "avete", "hanno"),
            "andare" to listOf("vado", "vai", "va", "andiamo", "andate", "vanno")
        )

        assertTrue(multiMap.containsKey("essere"))

        assertTrue(multiMap.containsValue("abbiamo"))

        multiMap.putValues("essere",
            "ero", "eri", "era", "eravamo", "eravate", "erano")

        assertTrue(multiMap.containsValue("eravamo"))
        assertFalse(multiMap.containsValue("avevi"))

    }

}