package me.amplitudo.inventar.web.rest.errors;

import org.springframework.context.annotation.Description;
import org.zalando.problem.AbstractThrowableProblem;

@Description(value = "Bad request replacement exception.")
public class BadActionException extends AbstractThrowableProblem {

    private final transient String code;
    private final transient String description;

    public BadActionException(String code, String description)
    {
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
