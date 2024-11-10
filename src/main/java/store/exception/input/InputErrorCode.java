package store.exception.input;

import store.exception.ErrorCode;

public enum InputErrorCode implements ErrorCode {
    INVALID_INPUT_FORM("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    private final String errorMessage;

    InputErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
