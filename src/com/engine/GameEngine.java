package com.engine;


import com.character.Player;
import com.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;



public class GameEngine {
    boolean winStatus = false;
    boolean loseStatus = false;
    String currentLocation = "landing dock";
    Player player;
    //NPC NPCs;
    //NPC zombies; ?? later for tracking how many are alive and where?
    HashMap<String, String> catalog = new HashMap<>();

    // Setting up the different rooms in the spaceship
    HashMap<String, HashMap<String, String>> spaceship = new HashMap<>();

    //Create a landing dock room
    HashMap<String, String> landingDock = new HashMap<>();

    //Create a hall room
    HashMap<String, String> hall = new HashMap<>();

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


        //Create a door to go north
        landingDock.put("north", "hall");

        //Create a door to go south
        hall.put("south", "landing dock");

        //Adding the two rooms to the spaceship object
        spaceship.put("landing dock", landingDock);
        spaceship.put("hall", hall);
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
                    System.out.println("You're looking.");
                    break;
                case "hit":
                    System.out.println("You're hitting.");
                    break;
                case "go":
                    //check that this room is accessible from current room
                    player.setLocation(command[1]);
                    System.out.println("You're in the " + command[1]);
                    if (command[1].equals("north")){
                        if (spaceship.get(currentLocation).containsKey("north"))
                            currentLocation = spaceship.get(currentLocation).get("north");
                        else
                            System.out.println("Cannot go north");
                    }

                    if (command[1].equals("south")){
                        if (spaceship.get(currentLocation).containsKey("south"))
                            currentLocation = spaceship.get(currentLocation).get("south");
                        else
                            System.out.println("Cannot go south");
                    }

                    if (command[1].equals("east")){
                        if (spaceship.get(currentLocation).containsKey("east"))
                            currentLocation = spaceship.get(currentLocation).get("east");
                        else
                            System.out.println("Cannot go east");
                    }

                    if (command[1].equals("west")){
                        if (spaceship.get(currentLocation).containsKey("west"))
                            currentLocation = spaceship.get(currentLocation).get("west");
                        else
                            System.out.println("Cannot go west");
                    }
                    break;
                case "get":
                    Item newItem = new Item(command[1], player.getLocation());
                    if (catalog.containsKey(command[1]) && catalog.get(command[1]) == currentLocation) {
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


    public static void showStatus(String location) {
        System.out.println("You are currently in the " + location);
        System.out.println();
        System.out.println("Where do you want to go?");
        System.out.println("Commands:");
        System.out.println("go north");
        System.out.println("go south");
        System.out.println("go east");
        System.out.println("go west");
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

    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.runGameLoop();
    }
}