package com.item;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Item {

    private String name;
    private String location; // until location is a class?
    private String description;

    /**
     *
     * @return catalog hashmap
     */
    public static HashMap<String, Item> readAll() {
        JSONParser parser = new JSONParser();
        HashMap<String, Item> catalog = new HashMap<>();
        try {
            JSONObject characterSet = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            for (Object room : characterSet.keySet()) {
                JSONObject roomObj = (JSONObject) characterSet.get(room);
                JSONArray itemArr = (JSONArray) roomObj.get("Item");
                if (itemArr != null) {
                    for (Object item : itemArr) {
                        catalog.put(item.toString(), new Item(item.toString(), room.toString()));
                    }
                }
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(catalog.keySet());
        return catalog;
    }


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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

}