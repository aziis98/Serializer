package com.aziis98.serializer

import com.aziis98.utils.*
import java.io.*
import java.util.*

// Copyright 2016 Antonio De Lucreziis

fun <T> OutputStream.writeCollection(collection: Collection<T>, serializer: OutputStream.(T) -> Unit) {
    writeInt(collection.size)
    collection.forEach {
        serializer(it)
    }
}

fun <T> InputStream.readCollection(deserializer: InputStream.() -> T): Collection<T> {
    val len = readInt()
    val list = mutableListOf<T>()

    repeat(len) {
        list += deserializer()
    }

    return list
}

fun <K, V> OutputStream.writeMap(map: Map<K, V>,
                                 keySerializer: OutputStream.(K) -> Unit,
                                 valueSerializer: OutputStream.(V) -> Unit) {
    writeInt(map.size)
    map.forEach { entry ->
        keySerializer(entry.key)
        valueSerializer(entry.value)
    }
}

fun <K, V> InputStream.readMap(keyDeserializer: InputStream.() -> K,
                               valueDeserializer: InputStream.() -> V): Map<K, V> {
    val len = readInt()
    val map = HashMap<K, V>()

    repeat(len) {
        map.put(keyDeserializer(), valueDeserializer())
    }

    return map
}

fun <K, V> OutputStream.writeMultiMap(multiMap: MultiMap<K, V>,
                                      keySerializer: OutputStream.(K) -> Unit,
                                      valueSerializer: OutputStream.(V) -> Unit) {
    writeMap(multiMap.map, keySerializer) {
        writeCollection(it, valueSerializer)
    }
}

fun <K, V> InputStream.readMultiMap(keyDeserializer: InputStream.() -> K,
                                    valueDeserializer: InputStream.() -> V): MultiMap<K, V> {
    return multiMapOfMap(readMap(keyDeserializer) {
        readCollection(valueDeserializer).toList()
    })
}


fun OutputStream.writeByteArray(byteArray: ByteArray) {
    writeInt(byteArray.size)
    write(byteArray)
}

fun InputStream.readByteArray(): ByteArray {
    val len = readInt()
    return ByteArray(len) {
        readByte()
    }
}

enum class ArraySize {
    Byte, Short, Int
}

fun <T> OutputStream.writeArray(array: Array<T>, typeSize: ArraySize = ArraySize.Int, serializer: OutputStream.(T) -> Unit) {
    when (typeSize) {
        ArraySize.Byte ->
            writeByte(array.size)
        ArraySize.Short ->
            writeShort(array.size)
        ArraySize.Int ->
            writeInt(array.size)
    }

    array.forEach {
        serializer(it)
    }
}

inline fun <reified T> InputStream.readArray(typeSize: ArraySize = ArraySize.Int, deserializer: InputStream.() -> T): Array<T> {
    val len = when (typeSize) {
        ArraySize.Byte ->
            readByte().toInt()
        ArraySize.Short ->
            readShort().toInt()
        ArraySize.Int ->
            readInt()
    }

    return Array(len) {
        deserializer()
    }
}