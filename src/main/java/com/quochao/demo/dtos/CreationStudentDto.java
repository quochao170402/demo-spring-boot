package com.quochao.demo.dtos;

import java.time.LocalDate;

public class CreationStudentDto {
    private String name;
    private String email;

    public CreationStudentDto() {
    }

    public CreationStudentDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CreationStudentDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
