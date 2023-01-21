package com.example.videoklub.model;

import java.util.ArrayList;

public class Movie {
    long id;
    String name;
    String releaseDate;
    ArrayList<Actor> actors;
    Director director;

    public Movie() {
        this.actors = new ArrayList<>();
    }

    public Movie(String name, String releaseDate, ArrayList<Actor> actors, Director director) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.actors = actors;
        this.director = director;
    }

    public Movie(String name, String releaseDate) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.actors = new ArrayList<>();
    }

    public Movie(long id, String name, String releaseDate) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.actors = new ArrayList<>();
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public void addActor(Actor actor){
        this.actors.add(actor);
    }
}
