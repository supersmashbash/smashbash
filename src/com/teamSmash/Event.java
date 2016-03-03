package com.teamSmash;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by branden on 3/3/16 at 18:56.
 */
public class Event {

    private String name, location, image, description;
    private LocalTime time;
    private LocalDate date;
    private int id;


    public Event(int id, String name, String location, LocalTime time, LocalDate date, String image, String description) {

        setId(id);
        setName(name);
        setLocation(location);
        setTime(time);
        setDate(date);
        setImage(image);
        setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}