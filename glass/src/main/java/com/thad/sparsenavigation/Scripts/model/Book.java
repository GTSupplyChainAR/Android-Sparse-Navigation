package com.thad.sparsenavigation.Scripts.model;

public class Book {
    private String name;
    private String author;
    private String locationTag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    @Override
    public String toString() {
        String str = "";
        str += "Name: " + name + ", ";
        str += "Author: " + author + ", ";
        str += "LocationTag: " + locationTag + "\n";
        return str;
    }
}