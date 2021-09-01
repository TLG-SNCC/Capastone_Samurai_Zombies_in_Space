package com.item;

public class Weapon extends Item {

    public Weapon(String name, String location) {
        super(name, location);
    }

    public boolean isWeapon() {
        return true;
    }
}