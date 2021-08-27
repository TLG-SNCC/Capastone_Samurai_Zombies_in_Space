package com.engine;


import java.util.Scanner;

public class GameEngine {
    boolean winStatus = false;
    boolean loseStatus = false;
    String currentLocation = "Loading Dock";
    //Player player;
    //NPC npcs;
    //NPC zombies; ?? later for tracking how many are alive and where?
    //Location locations;
    //Item items;

    public GameEngine() {
        // Set up Player
        // player = new Player;
        // Get NPCs
        // npcs = new NPC();

        // Get Locations
        // locations = new Location();

        // Get Items
        // items = new Item();
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
            switch (command[0]) {
                case "look":
                    System.out.println("You're looking.");
                    break;
                case "hit":
                    System.out.println("You're hitting.");
                    break;
                case "go":
                    System.out.println("You're going.");
                    break;
                case "get":
                    System.out.println("You're getting.");
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