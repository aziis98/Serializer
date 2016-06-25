package com.aziis98.serializer

import java.io.*

// Copyright 2016 Antonio De Lucreziis

fun OutputStream.writeString(string: String, typeSize: ArraySize = ArraySize.Int) {
    writeArray<Byte>(string.toByteArray().toTypedArray(), typeSize, OutputStream::writeByte)
}

fun InputStream.readString(typeSize: ArraySize = ArraySize.Int): String {
    return String(readArray(typeSize, InputStream::readByte).toByteArray())
}

fun OutputStream.writeObject(obj: Any?) {
    if (obj == null) {
        writeString("null", ArraySize.Short)
    }
    else {
        val typeName = obj.javaClass.kotlin.qualifiedName!!
        writeString(typeName, ArraySize.Short)
        Serializer.serializers[typeName]!!(obj)
    }
}

fun InputStream.readObject(): Any? {
    val typeName = readString(ArraySize.Short)

    if(typeName == "null")
        return null
    else
        return Serializer.deserializers[typeName]!!()
}