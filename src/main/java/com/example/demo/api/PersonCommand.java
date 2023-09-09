package com.example.demo.api;

import com.example.demo.domain.PersonEntity;
import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PersonCommand {

    private @NotBlank(message = "the nickname is mandatory") String nickname;
    private @NotBlank(message = "the name is mandatory") String name;
    private @NotBlank(message = "the birthday is mandatory") String birthdate;
    private List<String> stack;

    public PersonCommand(String nickname, String name, String birthdate, List<String> stack) {
        this.nickname = nickname;
        this.name = name;
        this.birthdate = birthdate;
        this.stack = stack;
    }

    public PersonEntity toEntity() {
        return new PersonEntity(nickname, name, birthdate, stack);
    }

    public boolean  isNotValid() {
        return nickname == null && name == null;
    }

    public boolean isNotSyntacticallyInvalid() {
        var pattern = Pattern.compile("[a-zA-Z]");
        var matcher = pattern.matcher(name);

        return matcher.matches();

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }

    public static void main(String[] args) {
        var p = new PersonCommand("ab", "joaquina", "07-2-1934", Arrays.asList("java", "ruby"));
        System.out.println("Is valid: " + p.isNotSyntacticallyInvalid());
    }
}


