package com.aziis98.serializer

import java.io.*
import java.nio.ByteBuffer

// Copyright 2016 Antonio De Lucreziis

fun <T> OutputStream.writeValue(obj: T, serializer: OutputStream.(T) -> Unit) {
    serializer(obj)
}

fun <T> InputStream.readValue(deserializer: InputStream.() -> T) {
    deserializer()
}

fun OutputStream.writeNull() {
    writeByte(0)
}

inline fun <reified R> InputStream.readNull(): R? {
    assert(readByte() == 0.toByte())
    return null
}

fun InputStream.readByte() = read().toByte()

fun InputStream.readShort() = ByteBuffer.wrap(byteArrayOf(readByte(), readByte())).short

fun InputStream.readInt() = ByteBuffer.wrap(byteArrayOf(readByte(), readByte(), readByte(), readByte())).int

fun InputStream.readLong() = ByteBuffer.wrap(byteArrayOf(readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte(), readByte())).long


fun OutputStream.writeByte(byte: Int) {
    write(byte)
}

fun OutputStream.writeShort(short: Int) {
    write(((short shr 8) and 0xFF).toInt())
    write(((short shr 0) and 0xFF).toInt())
}

fun OutputStream.writeInt(int: Int) {
    write(((int shr 24) and 0xFF).toInt())
    write(((int shr 16) and 0xFF).toInt())
    write(((int shr 8) and 0xFF).toInt())
    write(((int shr 0) and 0xFF).toInt())
}

fun OutputStream.writeLong(long: Long) {
    write(((long shr 56) and 0xFF).toInt())
    write(((long shr 48) and 0xFF).toInt())
    write(((long shr 40) and 0xFF).toInt())
    write(((long shr 32) and 0xFF).toInt())
    write(((long shr 24) and 0xFF).toInt())
    write(((long shr 16) and 0xFF).toInt())
    write(((long shr 8) and 0xFF).toInt())
    write(((long shr 0) and 0xFF).toInt())
}

fun OutputStream.writeByte(byte: Byte) = writeByte(byte.toInt())

fun OutputStream.writeShort(short: Short) = writeShort(short.toInt())