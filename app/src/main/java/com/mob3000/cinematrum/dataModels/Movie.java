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
    private String rating;
    private String movieTrailerURL;
    private String duration;

    public String getMovieTrailerURL() {
        return movieTrailerURL;
    }
    public void setMovieTrailerURL(String movieTrailerURL) {
        this.movieTrailerURL = movieTrailerURL;
    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
    public String getCategoryNames(){
        String string="";
        for (int i=0; i<categories.size(); i++)
        {
            string=string+categories.get(i).getName()+"\n";
        }
        return string;
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
    public String getDuration() {return duration;}

    public void setDuration(String duration) {this.duration = duration;}

    @Override
    public String toString() {
        return "Movie{" +
                "movie_id=" + movie_id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", publishedDate=" + publishedDate +
                ", rating='" + rating + '\'' +
                ", moviesCinemas=" + moviesCinemas +
                ", categories=" + categories +
                ", duration=" + duration +

                '}';
    }
}
