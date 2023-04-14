package utils

import com.squareup.moshi.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class UuidAdapter : JsonAdapter<UUID>() {
    @FromJson
    override fun fromJson(reader: JsonReader): UUID? = UUID.fromString(reader.readJsonValue().toString())

    @ToJson
    override fun toJson(writer: JsonWriter, value: UUID?) {
        writer.jsonValue(value.toString())
    }
}

class LocalDateAdapter : JsonAdapter<LocalDate>() {
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDate? = LocalDate.parse(reader.readJsonValue().toString())

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        writer.jsonValue(value.toString())
    }
}

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDateTime? = LocalDateTime.parse(reader.readJsonValue().toString())

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.jsonValue(value.toString())
    }
}


fun <T> JsonAdapter<T>.toPrettyJson(value: T) = this.indent("  ").toJson(value)
