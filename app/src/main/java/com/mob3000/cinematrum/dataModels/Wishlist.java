package com.mob3000.cinematrum.dataModels;

public class Wishlist {

    // TODO: user_id instead of User object?
    // Does everyone know what one item of the wishlist represents?

    private int wishlist_id;
    private int user_id;
    private int movie_id;
    private Movie _movie;


    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }


    public Movie get_movie() {
        return _movie;
    }

    public void set_movie(Movie _movie) {
        this._movie = _movie;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "wishlist_id=" + wishlist_id +
                ", user_id=" + user_id +
                ", movie_id=" + movie_id +
                '}';
    }
}
