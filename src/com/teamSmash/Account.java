package com.teamSmash;

/**
 * Created by branden on 3/3/16 at 18:55.
 */
public class Account {

    private String name, password;
    private int id;

    public Account(String name, String password, int id) {
        setName(name);
        setPassword(password);
        setId(id);
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
}