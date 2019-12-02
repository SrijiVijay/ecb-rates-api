package me.vitblokhin.ecbratesapi.dto.json;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class ExceptionDto implements Serializable {
    private String message;
    private List<String> errors;

    public ExceptionDto(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public ExceptionDto(String message, String error) {
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
} // class ExceptionDto
