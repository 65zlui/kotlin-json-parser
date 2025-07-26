package io.github._65zlui.jsonparser

object Json {
    /**
     * Parses the given JSON string into a JsonValue object.
     * @param jsonString The JSON string to parse.
     * @return A JsonValue representing the parsed JSON structure.
     * @throws JsonParseException if the input string is not a valid JSON.
     */
    fun parse(jsonString: String): JsonValue {
        val lexer = JsonLexer(jsonString)
        val parser = JsonParser(lexer)
        return parser.parse()
    }
}
