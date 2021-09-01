package com.item;

import java.util.HashMap;

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
     * Code yanked unceremoniously from
     * tabnine.com/code/java/methods/org.json.simple.JSONObject/entrySet
     *
     * @return catalog hashmap
     */
    public static HashMap<String, String> readAll() {
        JSONParser parser = new JSONParser();
        HashMap<String, String> catalog = new HashMap<>();
        try {
            JSONArray items = (JSONArray) parser.parse(new FileReader("cfg/Items.json"));
            for (Object item : items) {
                JSONObject jsonObject = (JSONObject) item;
                for (Object jsonEntry : jsonObject.entrySet()) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) jsonEntry;
                    catalog.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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