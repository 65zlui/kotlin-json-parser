package io.github._65zlui.jsonparser

class JsonLexer(private val jsonString: String) {
    private var position = 0 // Current reading position in the string

    /** Skips any whitespace characters. */
    private fun skipWhitespace() {
        while (position < jsonString.length && jsonString[position].isWhitespace()) {
            position++
        }
    }

    /** Peeks at the next character without advancing the position. */
    private fun peek(): Char? {
        return if (position < jsonString.length) jsonString[position] else null
    }

    /** Consumes the current character and advances the position. */
    private fun consume(): Char {
        return jsonString[position++]
    }

    /** Reads a string token, handling escape sequences. */
    private fun readString(): Token {
        val start = position
        consume() // Consume the opening quote "
        val builder = StringBuilder()
        while (peek() != '"' && peek() != null) {
            val char = consume()
            if (char == '\\') { // Handle escape characters
                when (val escapedChar = consume()) {
                    '"', '\\', '/' -> builder.append(escapedChar)
                    'b' -> builder.append('\b')
                    'f' -> builder.append('\u000C') // Form feed
                    'n' -> builder.append('\n')
                    'r' -> builder.append('\r')
                    't' -> builder.append('\t')
                    'u' -> { // Unicode escape sequence \uXXXX
                        val hex = jsonString.substring(position, position + 4)
                        position += 4
                        builder.append(hex.toInt(16).toChar())
                    }
                    else -> throw JsonParseException("Invalid escape sequence: \\$escapedChar")
                }
            } else {
                builder.append(char)
            }
        }
        if (peek() != '"') {
            throw JsonParseException("Unterminated string at position $position")
        }
        consume() // Consume the closing quote "
        return Token(TokenType.STRING, builder.toString())
    }

    /** Reads a number token. */
    private fun readNumber(): Token {
        val start = position
        // Characters that can be part of a number: digits, '.', '-', 'e', 'E', '+'
        while (peek()?.let { it.isDigit() || it == '.' || it == '-' || it == 'e' || it == 'E' || it == '+' } == true) {
            consume()
        }
        val value = jsonString.substring(start, position)
        return Token(TokenType.NUMBER, value)
    }

    /** Reads a keyword (true, false, null). */
    private fun readKeyword(keyword: String, tokenType: TokenType): Token {
        if (jsonString.regionMatches(position, keyword, 0, keyword.length)) {
            position += keyword.length
            return Token(tokenType)
        }
        throw JsonParseException("Unexpected token at position $position: expected '$keyword'")
    }

    /**
     * Returns the next token from the JSON string.
     * @throws JsonParseException if an unexpected character or malformed token is encountered.
     */
    fun nextToken(): Token {
        skipWhitespace()

        if (position >= jsonString.length) {
            return Token(TokenType.EOF)
        }

        return when (val char = peek()) {
            '{' -> { consume(); Token(TokenType.CURLY_OPEN)
            }
            '}' -> { consume(); Token(TokenType.CURLY_CLOSE)
            }
            '[' -> { consume(); Token(TokenType.SQUARE_OPEN)
            }
            ']' -> { consume(); Token(TokenType.SQUARE_CLOSE)
            }
            ':' -> { consume(); Token(TokenType.COLON)
            }
            ',' -> { consume(); Token(TokenType.COMMA)
            }
            '"' -> readString()
            '-', in '0'..'9' -> readNumber()
            't' -> readKeyword("true", TokenType.BOOLEAN_TRUE)
            'f' -> readKeyword("false", TokenType.BOOLEAN_FALSE)
            'n' -> readKeyword("null", TokenType.NULL)
            else -> throw JsonParseException("Unexpected character: '$char' at position $position")
        }
    }
}