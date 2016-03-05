package com.teamSmash;

/**
 * Created by branden on 3/3/16 at 18:56.
 */
public class Event {

    private String name, location, image, description;
    private String time;
    private String date;
    private int id;
    private int eventOwner;


    public Event(int id, String name, String location, String time, String date, String image, String description, int eventOwner) {

        setId(id);
        setName(name);
        setLocation(location);
        setTime(time);
        setDate(date);
        setImage(image);
        setDescription(description);
        setEventOwner(eventOwner);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(int eventOwner) {
        this.eventOwner = eventOwner;
    }
}