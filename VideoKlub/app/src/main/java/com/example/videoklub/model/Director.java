package com.example.videoklub.model;

public class Director {
    long id;
    String name;
    String birthDate;

    public Director() {

    }

    public Director(String name, String birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Director(long id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
