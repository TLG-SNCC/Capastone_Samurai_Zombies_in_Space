package com.engine;


import com.character.Player;
import com.item.Item;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.item.Weapon;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GameEngine {
    boolean winStatus = false;
    boolean loseStatus = false;
    static String currentLocation = "Landing Dock";
    static Player player;
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

        // Get Items
        catalog.put("spacewrench", "landing dock");
        catalog.put("lever", "hall");
        catalog.put("katana", "bar");



        //Create a door to go north
//        landingDock.put("north", "hall");
//
//        //Create a door to go south
//        hall.put("south", "landing dock");
//
//        //Create a room east of the hall leading to the bar
//        hall.put("east","bar");
//
//        //Create a room west of the bar leading to the hall
//        bar.put("west","hall");
//
//        //Adding the two rooms to the spaceship object
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
                if (command[0].equals("Q")) {
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
                    System.out.println("You're looking.");
                    break;
                case "hit":
                    System.out.println("You're hitting.");
                    break;
                case "Go":
                    headToNextRoom(command[1]);
                    //check that this room is accessible from current room
                    player.setLocation(currentLocation);
                    break;
                case "Get":
                    if (command.length == 3) {
                        pickUpItem(command[1] + " " + command[2]);
                        break;
                    }
                    if (command.length == 2) {
                        pickUpItem(command[1]);
                        break;
                    }
                    else
                        System.out.println("Sorry, Dave. I can't do that.");
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
                System.out.print(item.getName() + "; ");
            }
            System.out.println();

        }
    }


    public static void showStatus(String location) {
        System.out.println("You are currently in the " + location);
        System.out.println();
        System.out.println("Where do you want to go?");
        System.out.println("Commands: go north, south, east, west");
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
        String[] stringArr = input.toLowerCase().split("[\\s]+");
        for (int i = 0; i < stringArr.length; i++)
            stringArr[i] = stringArr[i].substring(0,1).toUpperCase() + stringArr[i].substring(1);
        return stringArr;
    }

    public static void headToNextRoom(String direction) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
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

    public static void pickUpItem(String thing) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
            JSONArray itemsInRoom = (JSONArray) current.get("Item");

            if (itemsInRoom.contains(thing)) {
                System.out.println("Placed " + thing + " in your inventory");
                Item itemToGet = new Item(thing, currentLocation);
                player.addToInventory(itemToGet);
            }
                    else
                        System.out.println(thing + " is already in your inventory");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Item doesn't exist");
        }
    }

    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.runGameLoop();
    }
}