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
public enum Player {
    PLAYER;

    List<Item> inventory;
    Integer health;
    String location;  // until the locations are implemented

    /**
     * Constructor
     */
    Player() {
        this.inventory = new ArrayList<>();
        this.health = 20;
        this.location = "Loading Dock";
    }

    /**
     * Checks for presence of an item in inventory
     * @param item
     * @return item's presence as boolean
     */
    public boolean checkInventory(Item item) {
        return this.inventory.contains(item);
    }

    /**
     * Provides a List of items in inventory
     * @return List<Item>
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Checks that player is in the same room as the item's location, adds to inventory, and returns success status
     * @param item
     * @return boolean
     */
    public boolean addToInventory(Item item) {
        if (item.getLocation() == getLocation()) {
            this.inventory.add(item);
            return true;
        }
        return false;
    }

    // for testing, mostly
    public void clearInventory() {
        this.inventory = new ArrayList<>();
    }

    /**
     * Remove an item from inventory
     * @param item
     * @return String location of item
     */
    public String removeInventory(Item item) {
        this.inventory.remove(item);
        return item.getLocation();
    }

    /**
     * Get size of inventory
     * @return size of inventory
     */
    public Integer getInventorySize() {
        return this.inventory.size();
    }

    /**
     * Return current health level
     * @return health level
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * Assigns new number to player health level
     * @param health
     */
    public void setHealth(Integer health) {
        this.health = health;
    }

    /**
     * Returns player's current location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets player's location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}