package com.mob3000.cinematrum.dataModels;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Movie {

    private int movie_id;
    private String name;
    private String picture;
    private Date publishedDate;
    private List<MoviesCinemas> moviesCinemas;
    private List<Category> categories;

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

    public void setMoviesCinemas(List<MoviesCinemas> moviesCinemas) {
        this.moviesCinemas = moviesCinemas;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
