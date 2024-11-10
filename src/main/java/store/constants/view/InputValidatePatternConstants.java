package store.constants.view;

import java.util.regex.Pattern;

public enum InputValidatePatternConstants {
    VALID_INPUT_ITEM_PATTERN("^\\[[ㄱ-ㅎ가-힣a-zA-Z]+\\-([1-9][0-9]*)\\](,\\[[ㄱ-ㅎ가-힣a-zA-Z]+\\-([1-9][0-9]*)\\])*"),
    VALID_INPUT_ASK_ANSWER("[Y|N]");

    private final Pattern validatePattern;

    InputValidatePatternConstants(String validatePattern) {
        this.validatePattern = Pattern.compile(validatePattern);
    }

    public boolean isValid(String input) {
        return validatePattern.matcher(input).matches();
    }
}
