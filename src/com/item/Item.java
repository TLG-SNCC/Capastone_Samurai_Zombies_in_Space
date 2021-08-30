package com.item;

import org.json.simple.*;

public class Item {

    private String name;
    private String location; // until location is a class?
    private String description;

    public Item(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

}