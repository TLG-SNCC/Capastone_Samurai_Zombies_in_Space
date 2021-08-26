package com.character;

import com.item.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class to represent the single player in the game.
 * property: ArrayList of inventory items
 * property: Integer representing level of health
 * property: String representing current location of the player
 */
public class Player {

    List<Item> inventory;
    Integer health;
    String location;  // until the locations are implemented

    Player() {
        this.inventory = new ArrayList<>();
        this.health = 20;
        this.location = "dock";
    }

    public boolean checkInventory(Item item) {
        return this.inventory.contains(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(Item item) {
        this.inventory.add(item);
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}