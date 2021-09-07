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
    boolean fightingZombie;

    /**
     * Constructor
     */
    Player() {
        this.inventory = new ArrayList<>();
        this.health = 20;
        this.location = "Loading Dock";
        this.fightingZombie = false;
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
     * Checks for any item with a String name in inventory
     * @param name
     * @return boolean
     */
    public boolean checkInventoryName(String name) {
        boolean returnVal = false;
        for (Item item : getInventory()) {
            if (name.equals(item.getName())) {
                returnVal = true;
                break;
            }
        }
        return returnVal;
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
        this.inventory.add(item);
        return true;
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

    public boolean removeInventory(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equals(itemName)) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
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

    public void setFightingZombie(boolean fighting){
        this.fightingZombie = fighting;
    }

    public boolean getFightingZombie() {
        return fightingZombie;
    }

    public int attack(){
        return (int) (Math.random() * 5) + 1;
    }

    public void takeDamage(int damageTaken){
        int currentHp = getHealth() - damageTaken;
        setHealth(currentHp);
    }
}