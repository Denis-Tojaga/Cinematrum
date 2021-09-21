package com.mob3000.cinematrum.dataModels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {

    private int movie_id;
    private String name;
    private String description;
    private String picture;
    private Date publishedDate;
    private ArrayList<MoviesCinemas> moviesCinemas;
    private ArrayList<Category> categories;



    private String categoriesNamesConcat;

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<MoviesCinemas> getMoviesCinemas() {
        return moviesCinemas;
    }

    public void setMoviesCinemas(ArrayList<MoviesCinemas> moviesCinemas) {
        this.moviesCinemas = moviesCinemas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getCategoriesNamesConcat() {
        return categoriesNamesConcat;
    }

    public void setCategoriesNamesConcat(String categoriesNamesConcat) {
        this.categoriesNamesConcat = categoriesNamesConcat;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movie_id=" + movie_id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", publishedDate=" + publishedDate +
                ", moviesCinemas=" + moviesCinemas +
                ", categories=" + categories +
                '}';
    }
}
