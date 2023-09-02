package com.example.demo;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PersonCommand(
        @NotBlank(message = "the nickname is mandatory") String nickname,
        @NotBlank(message = "the name is mandatory") String name,
        @NotBlank(message = "the birthday is mandatory") String birthdate,
        List<String> stack
) {
    public PersonEntity toEntity() {
        return new PersonEntity(nickname, name, birthdate, stack);
    }
}


