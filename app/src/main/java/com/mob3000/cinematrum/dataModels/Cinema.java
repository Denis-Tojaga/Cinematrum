package com.mob3000.cinematrum.dataModels;

import java.util.List;

public class Cinema {

    private int cinema_id;
    private String name;
    private Float latitude;
    private Float longitude;
    private List<Hall> halls;

    public int getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(int cinema_id) {
        this.cinema_id = cinema_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
    }

    /*@Override
    public String toString() {
        return "Cinema{" +
                "cinema_id=" + cinema_id +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", halls=" + halls +
                '}';
    }*/
    @Override
    public String toString() {
        return name;
    }
}
