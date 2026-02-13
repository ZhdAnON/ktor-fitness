//package com.zhdanon.serializators
//
//import kotlinx.datetime.LocalDate
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.descriptors.*
//import kotlinx.serialization.encoding.*
//
//object LocalDateSerializer : KSerializer<LocalDate> {
//    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: LocalDate) {
//        encoder.encodeString(value.toString())
//    }
//
//    override fun deserialize(decoder: Decoder): LocalDate {
//        return LocalDate.parse(decoder.decodeString())
//    }
//}