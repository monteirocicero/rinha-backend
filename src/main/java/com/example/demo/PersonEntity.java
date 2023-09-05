package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String nickname;
    private String name;
    private String birthday;
    private List<String> stack;

    public PersonEntity(String nickname, String name, String birthdate, List<String> stack) {
        this.nickname = nickname;
        this.name = name;
        this.birthday = birthdate;
        this.stack = stack;
        this.id = UUID.randomUUID();
    }

    public PersonEntity(UUID id, String nickname, String name, String birthdate, List<String> stack) {
        this.nickname = nickname;
        this.name = name;
        this.birthday = birthdate;
        this.stack = stack;
        this.id = id;
    }

    public PersonEntity(){ }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public List<String> getStack() {
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(nickname, that.nickname) && Objects.equals(name, that.name) && Objects.equals(birthday, that.birthday) && Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, name, birthday, stack);
    }
}
