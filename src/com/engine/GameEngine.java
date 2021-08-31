package com.engine;


import com.character.Player;
import com.item.Item;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GameEngine {

    static String currentLocation = "Landing Dock";
    Player player;

    // gets player status
    public StringBuilder status = showStatus(currentLocation);

//    public StringBuilder gameBuilder;

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


//        //Create a door to go north
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

//    public StringBuilder gameStart(TextField input) {
//        gameBuilder.append(input.getText());
//        while(input != null){
//            System.out.println(gameBuilder);
//        }
//        return gameBuilder;
//        //System.out.println(GameSceneController.inputTextFieldString());
//    }

    public StringBuilder runGameLoop(String input) {
        StringBuilder gameBuilder = showStatus(currentLocation);

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
            if (command[0].equals("q")) {
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
                gameBuilder.append("You're looking.");
                //System.out.println("You're looking.");
                break;
            case "hit":
                System.out.println("You're hitting.");
                break;
            case "go":
                // Capitalize the directions so that it could read the JSON file
                String upper = command[1].substring(0, 1).toUpperCase() + command[1].substring(1);
                headToNextRoom(upper);
                //check that this room is accessible from current room
                player.setLocation(currentLocation);
                break;
            case "get":
                Item newItem = new Item(command[1], player.getLocation());
                if (catalog.containsKey(command[1]) && catalog.get(command[1]).equals(currentLocation)) {
                    player.addToInventory(newItem);
                } else {
                    gameBuilder.append("\n \"Sorry, Dave. I can't get that.\n");
                    //System.out.println("Sorry, Dave. I can't get that.");
                }
                break;
            case "talk":
                gameBuilder.append("\n you're talking. \n");
                //System.out.println("you're talking.");
                break;
        }

        //update win/lose status
        checkPlayerHealth();
        checkPuzzleComplete();

        //check for lose/win status
//            if (winStatus) {
//                System.out.println("You have won.");
//                break;
//            }
//
//            if (loseStatus) {
//                System.out.println("You have lost.");
//                break;
//            }

        System.out.print("Your inventory contains: ");
        for (Item item : player.getInventory()) {
            System.out.print(item.getName() + " ");
        }
        System.out.println();

        //}
        return gameBuilder;
    }


    public StringBuilder showStatus(String location) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n You are currently in the ")
                .append(location).append("\n Where do you want to go?")
                .append("\n Commands: \n Go North, \nGo South, \nGo East \nGo West. \n")
                .append("q to quit");
        //System.out.println(builder);
        return builder;
//        System.out.println("You are currently in the " + location);
//        System.out.println();
//        System.out.println("Where do you want to go?");
//        System.out.println("Commands:");
//        System.out.println("go north");
//        System.out.println("go south");
//        System.out.println("go east");
//        System.out.println("go west");
//        System.out.println("q to quit");
//        System.out.println();
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

    private void headToNextRoom(String direction) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
//            System.out.println(locations);
            JSONObject current = (JSONObject) locations.get(currentLocation);
//            System.out.println(medBay);
            String next = (String) current.get(direction);
            if (current.containsKey(direction)) {
                //gameBuilder.append(" \n Going ").append(direction);
                System.out.println("Going " + direction);
                currentLocation = next;
            } else
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


//    public static void main(String[] args) {
//        GameEngine game = new GameEngine();
//        game.runGameLoop();
//    }
}