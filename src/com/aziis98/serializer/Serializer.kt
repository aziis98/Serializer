package com.aziis98.serializer

import java.io.*
import java.nio.file.*

// Copyright 2016 Antonio De Lucreziis

object Serializer {

    val serializers   = hashMapOf<String, OutputStream.(Any) -> Unit>()
    val deserializers = hashMapOf<String, InputStream.() -> Any>()

    init {
        registerSerializer<String> { writeString(it) }
        registerDeserializer { readString() }

        registerSerializer<Int> { writeInt(it) }
        registerDeserializer { readInt() }

        registerSerializer<Long> { writeLong(it) }
        registerDeserializer { readLong() }
    }

    inline fun <reified R> registerSerializer(noinline func: OutputStream.(R) -> Unit) {
        serializers.put(R::class.qualifiedName!!) { func(it as R) }
    }

    inline fun <reified R> registerDeserializer(noinline func: InputStream.() -> R) {
        deserializers.put(R::class.qualifiedName!!) { func() as Any }
    }


    inline fun <T> save(obj: T, path: Path, serializer: OutputStream.(obj: T) -> Unit) {
        Files.newOutputStream(path).serializer(obj)
    }

    inline fun <T> load(path: Path, deserializer: InputStream.() -> T): T {
        return Files.newInputStream(path).deserializer()
    }

}
