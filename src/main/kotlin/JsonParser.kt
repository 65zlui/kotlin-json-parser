class JsonParser(private val lexer: JsonLexer) {
    private var currentToken: Token = lexer.nextToken() // The current token being processed

    /** Advances to the next token from the lexer. */
    private fun advance() {
        currentToken = lexer.nextToken()
    }

    /**
     * Consumes the current token if its type matches the expected type.
     * @param expectedType The TokenType expected.
     * @return The consumed Token.
     * @throws JsonParseException if the current token's type does not match the expected type.
     */
    private fun consume(expectedType: TokenType): Token {
        if (currentToken.type == expectedType) {
            val token = currentToken
            advance()
            return token
        }
        throw JsonParseException("Expected token type $expectedType but got ${currentToken.type}")
    }

    /**
     * Parses the entire JSON string into a JsonValue.
     * This is the entry point for parsing.
     */
    fun parse(): JsonValue {
        val value = parseValue()
        // Ensure that after parsing the main value, we are at the end of the file
        if (currentToken.type != TokenType.EOF) {
            throw JsonParseException("Unexpected tokens after root JSON value.")
        }
        return value
    }

    /** Parses any JSON value (object, array, string, number, boolean, null). */
    private fun parseValue(): JsonValue {
        return when (currentToken.type) {
            TokenType.CURLY_OPEN -> parseObject()
            TokenType.SQUARE_OPEN -> parseArray()
            TokenType.STRING -> parseString()
            TokenType.NUMBER -> parseNumber()
            TokenType.BOOLEAN_TRUE -> parseBoolean(true)
            TokenType.BOOLEAN_FALSE -> parseBoolean(false)
            TokenType.NULL -> parseNull()
            else -> throw JsonParseException("Unexpected token ${currentToken.type} while parsing value")
        }
    }

    /** Parses a JSON object. */
    private fun parseObject(): JsonValue.JsonObject {
        consume(TokenType.CURLY_OPEN)
        val members = mutableMapOf<String, JsonValue>()
        if (currentToken.type != TokenType.CURLY_CLOSE) { // Handle empty object {}
            while (true) {
                val keyToken = consume(TokenType.STRING)
                consume(TokenType.COLON)
                val value = parseValue()
                members[keyToken.value!!] = value

                if (currentToken.type == TokenType.COMMA) {
                    consume(TokenType.COMMA)
                    // After a comma, we must have another key, not a closing brace
                    if (currentToken.type == TokenType.CURLY_CLOSE) {
                        throw JsonParseException("Trailing comma in object before '}'")
                    }
                } else {
                    break // No comma, so we expect the closing brace
                }
            }
        }
        consume(TokenType.CURLY_CLOSE)
        return JsonValue.JsonObject(members)
    }

    /** Parses a JSON array. */
    private fun parseArray(): JsonValue.JsonArray {
        consume(TokenType.SQUARE_OPEN)
        val elements = mutableListOf<JsonValue>()
        if (currentToken.type != TokenType.SQUARE_CLOSE) { // Handle empty array []
            while (true) {
                elements.add(parseValue())
                if (currentToken.type == TokenType.COMMA) {
                    consume(TokenType.COMMA)
                    // After a comma, we must have another element, not a closing bracket
                    if (currentToken.type == TokenType.SQUARE_CLOSE) {
                        throw JsonParseException("Trailing comma in array before ']'")
                    }
                } else {
                    break // No comma, so we expect the closing bracket
                }
            }
        }
        consume(TokenType.SQUARE_CLOSE)
        return JsonValue.JsonArray(elements)
    }

    /** Parses a JSON string. */
    private fun parseString(): JsonValue.JsonString {
        val token = consume(TokenType.STRING)
        return JsonValue.JsonString(token.value!!) // Value is guaranteed for STRING token
    }

    /** Parses a JSON number. */
    private fun parseNumber(): JsonValue.JsonNumber {
        val token = consume(TokenType.NUMBER)
        // Attempt to parse as Long first, then as Double for floating-point numbers
        val numberValue: Number = token.value!!.toLongOrNull() ?: token.value.toDouble()
        return JsonValue.JsonNumber(numberValue)
    }

    private fun parseBoolean(value: Boolean): JsonValue.JsonBoolean {
        if (value) {
            consume(TokenType.BOOLEAN_TRUE)
        } else {
            consume(TokenType.BOOLEAN_FALSE)
        }
        return JsonValue.JsonBoolean(value)
    }

    private fun parseNull(): JsonValue.JsonNull {
        consume(TokenType.NULL)
        return JsonValue.JsonNull
    }
}
