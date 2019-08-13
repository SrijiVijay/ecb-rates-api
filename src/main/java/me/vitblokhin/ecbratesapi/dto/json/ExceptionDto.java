package me.vitblokhin.ecbratesapi.dto.json;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExceptionDto implements Serializable {
    private String message;

    public ExceptionDto(String message) {
        this.message = message;
    }
} // class ExceptionDto
