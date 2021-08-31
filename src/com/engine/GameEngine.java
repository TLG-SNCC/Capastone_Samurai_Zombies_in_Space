package com.engine;


import com.character.Player;
import com.item.Item;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GameEngine {
    private boolean winStatus = false;
    private boolean loseStatus = false;
    private static String currentLocation = "Landing Dock";
    private Player player;
    //NPC NPCs;
    //NPC zombies; ?? later for tracking how many are alive and where?
    HashMap<String, String> catalog = new HashMap<>();
    static JSONParser parser = new JSONParser();

    //Create a bar room
    HashMap<String, String> bar = new HashMap<>();

    public GameEngine() {
        // Set up Player
        player = Player.PLAYER;
        // Get NPCs
        // npcs = new NPC();

        // Get Locations
        // locations = new Location();

        // Get Items into Catalog
        catalog = Item.readAll();

        //Create a door to go north
        //landingDock.put("north", "hall");

        //Create a door to go south
        //hall.put("south", "landing dock");

        //Create a room east of the hall leading to the bar
        //hall.put("east","bar");

        //Create a room west of the bar leading to the hall
        bar.put("west","hall");

        //Adding the two rooms to the spaceship object
//        spaceship.put("landing dock", landingDock);
//        spaceship.put("hall", hall);
//        spaceship.put("bar", bar);

    }

    public void runGameLoop() {

        // Start loop
        while (!winStatus && !loseStatus) {
            //show status
            showStatus(currentLocation);

            // get user input
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            String[] command;
            command = parser(input);
            if (command.length < 2) {
                if (command[0].equals("q")) {
                    System.out.println("Exiting game");
                    System.exit(0);
                }
                else
                    System.out.println("Sorry, Dave. I can't do that.");
                continue;
            }

            // perform actions
            switch (command[0]) {
                case "look":
                    System.out.println(command[1]);
                    String response = getLookResult(command[1].strip().toLowerCase());
                    System.out.println(response);
                    break;
                case "hit":
                    System.out.println("You're hitting.");
                    break;
                case "go":
                    // Capitalize the directions so that it could read the JSON file
                    String upper = command[1].substring(0,1).toUpperCase() + command[1].substring(1);
                    headToNextRoom(upper);
                    //check that this room is accessible from current room
                    player.setLocation(currentLocation);
                    break;
                case "get":
                    Item newItem = new Item(command[1], player.getLocation());
                    if (catalog.containsKey(command[1]) && catalog.get(command[1]).equals(currentLocation)) {
                        player.addToInventory(newItem);
                    } else {
                        System.out.println("Sorry, Dave. I can't get that.");
                    }
                    break;
                case "talk":
                    System.out.println("you're talking.");
                    break;
            }

            //update win/lose status
            checkPlayerHealth();
            checkPuzzleComplete();

            //check for lose/win status
            if (winStatus) {
                System.out.println("You have won.");
                break;
            }

            if (loseStatus) {
                System.out.println("You have lost.");
                break;
            }

            System.out.print("Your inventory contains: ");
            for (Item item : player.getInventory()) {
                System.out.print(item.getName() + " ");
            }
            System.out.println();

        }
    }

    private String getLookResult(String objectToFind) {
        String object = objectToFind.strip().toLowerCase();
        String response = "";
        if (object.equals("around")) {
            response = "Don't look too hard now.";
        } else if (catalog.containsKey(object)) {
            response = catalog.get(object);
        } else {
            System.out.println(catalog);
            response = "You don't see a " + object + ".";
        }
        return response;
    }


    public static void showStatus(String location) {
        System.out.println("You are currently in the " + location);
        System.out.println();
        System.out.println("Where do you want to go?");
        System.out.println("Commands: go north, south, east, west");
//        System.out.println("go north");
//        System.out.println("go south");
//        System.out.println("go east");
//        System.out.println("go west");
        System.out.println("q to quit");
        System.out.println();
    }

    private void checkPlayerHealth() {
        //if Player.getHealth() < 1 { loseStatus = true; }
    }

    private void checkPuzzleComplete() {
        //if wizzlewhat and space wrench are in player inventory, winStatus = true
        //later, account for fueling time as well
    }

    private String[] parser(String input) {
        return input.toLowerCase().split("[\\s]+");
    }

    public static void headToNextRoom(String direction) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
//            System.out.println(locations);
            JSONObject current = (JSONObject) locations.get(currentLocation);
//            System.out.println(medBay);
            String next = (String) current.get(direction);
            if (current.containsKey(direction)) {
                System.out.println("Going " + direction);
                currentLocation = next;
            }
            else
                System.out.println("Can't go that way");
//            System.out.println(east);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.runGameLoop();
    }
}