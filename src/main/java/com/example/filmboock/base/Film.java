package com.example.filmboock.base;

import java.util.List;

public class Film {
    int id;
    String name;
    String image;
    String year;
    List<String> genre;
    String description;
    List<String> actor;
    String collection;
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActor(List<String> actor) {
        this.actor = actor;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getYear() {
        return year;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getActor() {
        return actor;
    }

    public String getCollection() {
        return collection;
    }

    public Film(int id, String name, String image, String year, List<String> genre, String description, List<String> actor, String collection) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.year = year;
        this.genre = genre;
        this.description = description;
        this.actor = actor;
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", year='" + year + '\'' +
                ", genre=" + genre +
                ", description='" + description + '\'' +
                ", actor=" + actor +
                ", collection='" + collection + '\'' +
                '}';
    }
}
