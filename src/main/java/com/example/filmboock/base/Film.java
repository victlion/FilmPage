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
