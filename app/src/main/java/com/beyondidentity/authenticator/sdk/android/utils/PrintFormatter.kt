package com.beyondidentity.authenticator.sdk.android.utils

fun Any?.toIndentString(includeSpace: Boolean = false): String {
    val notFancy = toString()
    return buildString(notFancy.length) {
        var indent = 0
        fun StringBuilder.line() {
            appendLine()
            repeat(2 * indent) { append(' ') }
        }

        for (char in notFancy) {
            if (char == ' ' && !includeSpace) continue

            when (char) {
                ')', ']' -> {
                    indent--
                    line()
                }
            }

            if (char == '=') append(' ')
            append(char)
            if (char == '=') append(' ')

            when (char) {
                '(', '[', ',' -> {
                    if (char != ',') indent++
                    line()
                }
            }
        }
    }
}
