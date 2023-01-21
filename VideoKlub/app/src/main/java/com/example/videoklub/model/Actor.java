package com.example.videoklub.model;


public class Actor {
    long id;
    String name;
    String birthDate;

    // constructors
    public Actor() {
    }

    public Actor(String name, String birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Actor(long id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // setters and getters
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}