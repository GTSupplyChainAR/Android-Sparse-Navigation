package com.thad.sparsenavigation.Scripts.model;
import java.util.List;

public class PickPath {
    private String pathId;
    private String type;
    private List<Book> booksInPath;
//    List<Book> training;
//    List<Book> testing;

    public String getPathId() {
        return pathId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Book> getBooksInPath() {
        return booksInPath;
    }

    public void setBooksInPath(List<Book> booksInPath) {
        this.booksInPath = booksInPath;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
//
//    public List<Book> getTraining() {
//        return training;
//    }
//
//    public void setTraining(List<Book> training) {
//        this.training = training;
//    }
//
//    public List<Book> getTesting() {
//        return testing;
//    }
//
//    public void setTesting(List<Book> testing) {
//        this.testing = testing;
//    }


    @Override
    public String toString() {
        String str = "";
        str += "PathId: " + pathId;
        str += "Type: " + type;
        str += "Books: " + booksInPath;
        return str;
    }
}