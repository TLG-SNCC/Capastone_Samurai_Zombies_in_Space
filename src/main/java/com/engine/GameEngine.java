package com.engine;

import com.character.NPC;
import com.character.Player;
import com.character.Zombie;
import com.item.Item;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.List;

public class GameEngine {

    public String currentLocation = "Landing Dock";
    private final Player player = Player.PLAYER;

    //public StringBuilder status = showStatus(currentLocation);
    public List<Item> inventory;

    //NPC zombies; ?? later for tracking how many are alive and where?
    HashMap<String, Item> catalog = Item.readAll();
    static JSONParser parser = new JSONParser();

    //Create a bar room
    HashMap<String, String> bar = new HashMap<>();

    public StringBuilder runGameLoop(String input) {
        StringBuilder gameBuilder = new StringBuilder();
        inventory = player.getInventory();

        String[] command;
        command = Parser.parseInput(input);

        // perform actions
        switch (command[0]) {
            case "q":
                gameBuilder.append("Exiting game");
                System.exit(0);
                //TODO: Exit game scene without closing whole game
                break;
            case "look":
                if (command[1] == null || command[1].isBlank() || command[1].equals("around")) {
                    System.out.println(command[1]);
                    gameBuilder.append(examineRoom());
                } else {
                    gameBuilder.append(getLookResult(command[1]));
                }
                break;
            case "go":
                gameBuilder.append(headToNextRoom(command[1]));
                //check that this room is accessible from current room
                player.setLocation(currentLocation);
                break;
            case "get":
                if (command.length > 1) {
                    gameBuilder.append(pickUpItem((command[1])));
                } else {
                    gameBuilder.append("\n \"Sorry, Dave. I can't get that.\n");
                }
                break;
            case "drop":
               gameBuilder.append(dropItem(command[1]));
               break;
            case "talk":
                NPC character = new NPC(command[1]);
                gameBuilder.append(character.getDialogue());
                break;
            case "use":
                if (command.length == 2) {
                    if (command[1].equals("health kit") && player.checkInventoryName("health kit")) {
                        gameBuilder.append(healPlayer());
                    }
                    else if (command[1].equals("lever") && player.checkInventoryName("lever")){
                        gameBuilder.append("What should I use this on?");
                    } else {
                        gameBuilder.append("You can't do that Dave.");
                    }
                }
                break;
            case "fight":
                System.out.println(command[1].equals("zombie"));
                System.out.println(checkForZombies());
                if (checkForZombies() && (command.length == 1 || command[1].equals("zombie"))) {
                    Zombie zombie = new Zombie(6, currentLocation);
                    player.setFightingZombie(true);
                    while (player.getFightingZombie()) {
                        gameBuilder.append(fightLoop(zombie));
                    }
                } else {
                    gameBuilder.append("\nDave, stay focused. You can pick a fight later.");
                }
                break;
            case "help":
                gameBuilder.append(showInstructions());
                break;
            case "catalog":
                gameBuilder.append(reviewCatalog());
                break;
            case "inventory":
                gameBuilder.append(reviewInventory());
                break;
            default:
                gameBuilder.append("Sorry, Dave. I can\'t do that.");

        }

        //update win/lose status
        checkPlayerHealth();
        checkPuzzleComplete();

        return gameBuilder.append(showStatus(currentLocation));
    }

    private Boolean checkForZombies() {
        try {
            JSONObject current = getJsonObject();
            String zombie = (String) current.get("enemy");
            if (zombie.equals("Zombie")) {
                return true;
            }
        } catch (NullPointerException e) {
            System.out.println("No zombies in " + currentLocation);
        }
        return false;
    }

    private StringBuilder fightLoop(Zombie zombie) {
        StringBuilder gameBuilder = new StringBuilder();

        Item zombieKatana = new Item("zombie katana", "Central Hub");
        gameBuilder.append("\nYou're attacking the zombie.");

        if (currentLocation.contains("Landing Dock")) {
            gameBuilder.append("\nPlayer: " + player.getHealth() + "HP. Zombie: " + zombie.getHealth() + "HP.");

            if (player.getHealth() > 0 && player.checkInventory(zombieKatana)) {
                zombie.takeDamage(zombie.attack() * 2);
                gameBuilder.append("\nYou swing your katana. Zomburai has " + zombie.getHealth() + "HP.");
            } else {
                zombie.takeDamage(zombie.attack());
                gameBuilder.append("\nYou used your fists. Zomburai has " + zombie.getHealth() + "HP.");
            }

//                        Thread.sleep(500); // Only works with sout. gameBuilder does a delay and then prints
            if (zombie.getHealth() > 0) {
                player.takeDamage(player.attack());
                gameBuilder.append("\nThe Zomburai hits you. You have " + player.getHealth() + "HP.");
            }


            if (player.getHealth() <= 0 || zombie.getHealth() <= 0) {
                if (player.getHealth() > 0) {
                    gameBuilder.append("\nYou managed to kill the Zomburai!\n");
                    player.setFightingZombie(false);
                } else {
                    gameBuilder.append("\nLooks like you've been killed. Womp womp.\n");
                    player.setFightingZombie(false);
                }
            }
        } else {
            gameBuilder.append("\nJust because you can, doesn't mean you should.");
        }
        return gameBuilder;
    }

    private StringBuilder showInstructions() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n Commands: ")
                .append("\n    Movement: go (north, south, east, west) \n")
                .append("\n    Observation: look")
                .append("\n    Get Information: talk")
                .append("\n    Actions: get, drop, fight, heal")
                .append("\n q to quit\n");
        return builder;
    }

    private String getLookResult(String objectToFind) {
        String object;
        if (objectToFind == null || objectToFind.equals("")) {
            object = "around";
        } else {
            object = objectToFind.strip().toLowerCase();
        }
        String response;
        if (object.equals("around")) {
            response = "Don't look too hard now.";
        } else if (catalog.containsKey(object)) {
            response = catalog.get(object).getDescription();
        }  else if (NPC.checkCast(object)) {
            NPC character = new NPC(objectToFind);
            response = character.getDescription();
        } else {
            response = "You don't see a " + object + ".";
        }
        return response + "\n";
    }


    public StringBuilder showStatus(String location) {
        StringBuilder builder = new StringBuilder();
        System.out.println(player.getAreasVisited());
        System.out.println("Zombies currently following you: " + player.getZombiesFollowing());
        builder.append("\n You are currently in the ")
                .append(location).append("\n\n");
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
        return input.toLowerCase().split("[\\s]+");
    }

    private String reviewCatalog() {
        String returnVal = " Items : [";
        for (Map.Entry<String, Item> item : catalog.entrySet()) {
            returnVal += item.getValue().getName() + " (in " + item.getValue().getLocation() + ") ";
        }
        return returnVal;
    }

    private String reviewInventory() {
        String returnVal = " Items : [";
        for (Item item : player.getInventory()) {
            returnVal += item.getName() + " (in " + item.getLocation() + ") ";
        }
        return returnVal;
    }

    private String examineRoom() {
        String description = "";
        try {
            JSONObject current = getJsonObject();
            description = (String) current.get("Description");
            List<Item> items = new ArrayList<>();
            for (Map.Entry<String, Item> item : catalog.entrySet()) {
                if (item.getValue().getLocation().equals(currentLocation)) {
                    items.add(item.getValue());
                }
            }
            if (items.size() > 0) {
                description += "\nItems in room: [";
                for (Item item : items) {
                    description += " " + item.getName() + " ";
                }
                description += "]\n";
            } else {
                description += "\nRuh roh. No items here!";
            }
            return description + "\n";
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return description + "]\n";
    }

    private String headToNextRoom(String direction) {
        try {
            JSONObject current = getJsonObject();
            String next = (String) current.get(direction);
            if (current.containsKey("enemy") && !player.checkAreasVisited(currentLocation)) {
                player.addZombiesFollowing();
                player.addAreasVisited(currentLocation);
            }
            if (current.containsKey(direction)) {
                currentLocation = next;
                return "You moved " + direction + "\n";
            }
        } catch (NullPointerException e) {
            System.out.println("Can't go that way\n");
        }
        return "Can't go that way\n";
    }

    private JSONObject getJsonObject() {
        JSONObject locations = new JSONObject();
        try {
            locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return (JSONObject) locations.get(currentLocation);
    }

    private String dropItem(String playerItem) {
        if (!player.checkInventoryName(playerItem)) {
            return "You can't drop what you don't have, Dave.";
        }

        //inventory.removeIf(item -> item.getName().equals(playerItem));
        catalog.replace(playerItem, new Item(playerItem, currentLocation)); //relocate the item
        player.removeInventory(playerItem);

        inventory = player.getInventory(); // refresh inventory

        // need to replace last item in list with empty item. removal of last item results in item being displayed in inventory container.
        if (inventory.size() <= 1) {
            Item emptyItem = new Item("", currentLocation);
            inventory.add(emptyItem);
        }

        return "You dropped the " + playerItem + " in the " + currentLocation;
    }

    private String healPlayer() {
        String response = "";
        StringBuilder builder = new StringBuilder();
        if (player.getHealth() >= 30) {
            response = "\nYou're at max health.";
        } else {
            player.setHealth(player.getHealth() + 5);
            response = "\nYour current health is: " + player.getHealth() + "HP. ";
        }
        return response;
    }

    private String pickUpItem(String thing) {
        Item item = catalog.get(thing);
        if (item != null && item.getLocation().equals(currentLocation)) {
            item.setLocation("player");
            player.addToInventory(item);
            catalog.replace(thing, item);
            return "Placed " + thing + " in your inventory\n";
        } else if (item != null && item.getLocation().equals("player")) {
            return "You already have the " + thing;
        }
        return thing + " doesn't exist\n";
    }
}