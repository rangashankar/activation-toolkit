package com.acme.activation.qr;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SimpleJson {
    private SimpleJson() {
    }

    public static String stringify(Map<String, Object> values) {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!first) {
                builder.append(',');
            }
            first = false;
            builder.append('"').append(escape(entry.getKey())).append('"').append(':');
            Object value = entry.getValue();
            if (value instanceof Number) {
                builder.append(value);
            } else {
                builder.append('"').append(escape(String.valueOf(value))).append('"');
            }
        }
        builder.append('}');
        return builder.toString();
    }

    public static Map<String, String> parseFlatObject(String json) {
        String trimmed = json.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object");
        }
        String body = trimmed.substring(1, trimmed.length() - 1).trim();
        Map<String, String> result = new LinkedHashMap<>();
        int index = 0;
        while (index < body.length()) {
            index = skipWhitespace(body, index);
            if (index >= body.length()) {
                break;
            }
            if (body.charAt(index) != '"') {
                throw new IllegalArgumentException("Expected string key");
            }
            ParsedString key = parseString(body, index);
            index = skipWhitespace(body, key.nextIndex);
            if (index >= body.length() || body.charAt(index) != ':') {
                throw new IllegalArgumentException("Expected ':' after key");
            }
            index++;
            index = skipWhitespace(body, index);
            ParsedValue value = parseValue(body, index);
            result.put(key.value, value.value);
            index = skipWhitespace(body, value.nextIndex);
            if (index < body.length()) {
                if (body.charAt(index) == ',') {
                    index++;
                } else {
                    throw new IllegalArgumentException("Expected ',' between fields");
                }
            }
        }
        return result;
    }

    private static ParsedValue parseValue(String body, int index) {
        if (index >= body.length()) {
            throw new IllegalArgumentException("Unexpected end of JSON");
        }
        char ch = body.charAt(index);
        if (ch == '"') {
            ParsedString string = parseString(body, index);
            return new ParsedValue(string.value, string.nextIndex);
        }
        int start = index;
        while (index < body.length()) {
            ch = body.charAt(index);
            if (ch == ',' || Character.isWhitespace(ch)) {
                break;
            }
            index++;
        }
        String raw = body.substring(start, index);
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Expected value");
        }
        return new ParsedValue(raw, index);
    }

    private static ParsedString parseString(String body, int index) {
        if (body.charAt(index) != '"') {
            throw new IllegalArgumentException("Expected string");
        }
        StringBuilder builder = new StringBuilder();
        index++;
        while (index < body.length()) {
            char ch = body.charAt(index++);
            if (ch == '"') {
                return new ParsedString(unescape(builder.toString()), index);
            }
            if (ch == '\\') {
                if (index >= body.length()) {
                    throw new IllegalArgumentException("Invalid escape");
                }
                char escaped = body.charAt(index++);
                if (escaped == '"' || escaped == '\\' || escaped == '/') {
                    builder.append(escaped);
                } else if (escaped == 'b') {
                    builder.append('\b');
                } else if (escaped == 'f') {
                    builder.append('\f');
                } else if (escaped == 'n') {
                    builder.append('\n');
                } else if (escaped == 'r') {
                    builder.append('\r');
                } else if (escaped == 't') {
                    builder.append('\t');
                } else {
                    throw new IllegalArgumentException("Unsupported escape: \\" + escaped);
                }
            } else {
                builder.append(ch);
            }
        }
        throw new IllegalArgumentException("Unterminated string");
    }

    private static int skipWhitespace(String body, int index) {
        while (index < body.length() && Character.isWhitespace(body.charAt(index))) {
            index++;
        }
        return index;
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescape(String value) {
        return value;
    }

    private static final class ParsedString {
        private final String value;
        private final int nextIndex;

        private ParsedString(String value, int nextIndex) {
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }

    private static final class ParsedValue {
        private final String value;
        private final int nextIndex;

        private ParsedValue(String value, int nextIndex) {
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }
}
