package io.github._65zlui.jsonparser

sealed class JsonValue {
    /** Represents a JSON object, a map of string keys to JsonValue. */
    data class JsonObject(val members: Map<String, JsonValue>) : JsonValue()
    /** Represents a JSON array, a list of JsonValue. */
    data class JsonArray(val elements: List<JsonValue>) : JsonValue()
    /** Represents a JSON string. */
    data class JsonString(val value: String) : JsonValue()
    /** Represents a JSON number (can be Long or Double). */
    data class JsonNumber(val value: Number) : JsonValue()
    /** Represents a JSON boolean. */
    data class JsonBoolean(val value: Boolean) : JsonValue()
    /** Represents a JSON null value (singleton object). */
    object JsonNull : JsonValue()
}