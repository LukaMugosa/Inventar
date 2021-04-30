package me.amplitudo.inventar.web.rest.errors;

public enum ExceptionErrors {

    USER_NOT_FOUND("user-not-found", "User with given credentials was not found.");

    private final String code;
    private final String description;

    ExceptionErrors(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
