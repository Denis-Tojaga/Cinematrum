package com.mob3000.cinematrum.dataModels;

public class Wishlist {

    // TODO: user_id instead of User object?
    // Does everyone know what one item of the wishlist reoresents?

    private int wishilst_id;
    private int user_id;
    private int movie_id;

    public int getWishilst_id() {
        return wishilst_id;
    }

    public void setWishilst_id(int wishilst_id) {
        this.wishilst_id = wishilst_id;
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
}
