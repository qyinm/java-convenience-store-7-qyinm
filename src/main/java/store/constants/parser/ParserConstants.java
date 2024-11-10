package store.constants.parser;

import java.util.regex.Pattern;

public enum ParserConstants {
    PARSER_DELIMITER(",");

    private final Pattern pattern;

    ParserConstants(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
