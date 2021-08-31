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
import java.util.HashMap;

public class GameEngine {

    static String currentLocation = "Landing Dock";
    static Player player;
    public StringBuilder status = showStatus(currentLocation);

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


    }

    public StringBuilder runGameLoop(String input) {
        StringBuilder gameBuilder = new StringBuilder();


        // Start loop
//        boolean winStatus = false;
//        boolean loseStatus = false;
        // while (!winStatus && !loseStatus) {
        //show status
//            showStatus(currentLocation);

        // get user input
//            Scanner sc = new Scanner(System.in);
//            String input = sc.nextLine();

        String[] command;
        command = parser(input);
        if (command.length < 2) {
            if (command[0].equals("Q")) {
                gameBuilder.append("Exiting game");
                //System.out.println("Exiting game");
                System.exit(0);
                //TODO: Exit game scene without closing whole game
            } else
                gameBuilder.append("Sorry, Dave. I can't do that.");
            //System.out.println("Sorry, Dave. I can't do that.");
            //continue;
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
                } else {
                    gameBuilder.append("\n \"Sorry, Dave. I can't get that.\n");
                }
                break;
            case "talk":
                gameBuilder.append("\n you're talking. \n");
                break;
            case "heal":
                if (currentLocation.contains("Medical Bay")){
                    System.out.println("before " + player.getHealth());
                    player.setHealth(player.getHealth() + 5);
                    System.out.println("after " + player.getHealth());
                } else{
                    System.out.println("Sorry, Dave. You must be in the Medical Bay to heal.");
                }
        }

        //update win/lose status
        checkPlayerHealth();
        checkPuzzleComplete();

        //check for lose/win status
//            if (winStatus) {
//                System.out.println("You have won.");
//                break;
//            }

        gameBuilder.append("\n Your inventory contains:\n");
        for (Item item : player.getInventory()) {
            gameBuilder.append(item.getName()).append("; ");
        }
        gameBuilder.append(showStatus(currentLocation));
        //}
        return gameBuilder;
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


    public StringBuilder showStatus(String location) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n You are currently in the ")
                .append(location).append("\n Where do you want to go?")
                .append("\n Commands: \n Go North, \nGo South, \nGo East, \nGo West, \n")
                .append("q to quit\n");
        return builder;
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

    private void headToNextRoom(String direction) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
            String next = (String) current.get(direction);
            if (current.containsKey(direction)) {
                //gameBuilder.append(" \n Going ").append(direction);
                currentLocation = next;
            } else
                System.out.println("Can't go that way");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Can't go that way");
        }
    }

    private static void pickUpItem(String thing) {
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
}