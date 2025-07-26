package io.github._65zlui.jsonparser

enum class TokenType {
    CURLY_OPEN,      // {
    CURLY_CLOSE,     // }
    SQUARE_OPEN,     // [
    SQUARE_CLOSE,    // ]
    COLON,           // :
    COMMA,           // ,
    STRING,          // "..."
    NUMBER,          // 123, 12.3, -123
    BOOLEAN_TRUE,    // true
    BOOLEAN_FALSE,   // false
    NULL,            // null
    EOF              // End of File
}
