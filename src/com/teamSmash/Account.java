package com.teamSmash;

import java.util.ArrayList;

/**
 * Created by branden on 3/3/16 at 18:55.
 */
public class Account {

    private String name, password;
    private int id;
    private ArrayList<Event> events = new ArrayList<>();

    public Account( int id, String name, String password) {
        setId(id);
        setName(name);
        setPassword(password);

    }

    public Account(String password, String name) {
        this.password = password;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}