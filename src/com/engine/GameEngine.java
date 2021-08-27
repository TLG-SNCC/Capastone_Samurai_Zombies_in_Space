package com.engine;


import com.character.Player;
import com.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameEngine {
    boolean winStatus = false;
    boolean loseStatus = false;
    String currentLocation = "Loading Dock";
    Player player;
    //NPC npcs;
    //NPC zombies; ?? later for tracking how many are alive and where?
    List<Item> catalog = new ArrayList<>();

    public GameEngine() {
        // Set up Player
        player = Player.PLAYER;
        // Get NPCs
        // npcs = new NPC();

        // Get Locations
        // locations = new Location();

        // Get Items
        catalog.add(new Item("spacewrench", "engineering"));
        catalog.add(new Item("lever", "shuttlebay"));
    }

    public void runGameLoop() {

        // Start loop
        while (!winStatus && !loseStatus) {
            // get user input
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();


            // parse input 
            String[] command = new String[2];
            command = parser(input);
            if (command.length < 2) {
                System.out.println("Sorry, Dave. I can't do that.");
                continue;
            }

            // perform actions
            switch(command[0]) {
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
                    break;
                case "get":
                    Item newItem = new Item(command[1], player.getLocation());
                    player.addToInventory(newItem);
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
            System.out.println("");

        }
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