package com.engine;


import com.character.NPC;
import com.character.Player;
import com.character.Zombie;
import com.item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameEngine {

    private String currentLocation = "Landing Dock";
    private final Player player = Player.PLAYER;

    public StringBuilder status = showStatus(currentLocation);
    public List<Item> inventory;


    //NPC NPCs;
    //NPC zombies; ?? later for tracking how many are alive and where?
    HashMap<String, String> catalog = new HashMap<>();
    static JSONParser parser = new JSONParser();
    Zombie zombie = new Zombie(30, "Bar");

    //Create a bar room
    HashMap<String, String> bar = new HashMap<>();

    public StringBuilder runGameLoop(String input) {
        StringBuilder gameBuilder = new StringBuilder();
        inventory = player.getInventory();

        // Start loop
//        boolean winStatus = false;
//        boolean loseStatus = false;
        // while (!winStatus && !loseStatus) {

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
                if (command.length == 4) {
                    gameBuilder.append(dropItem(command[1] + " " + command[2] + " " + command[3]));
                    break;
                }
                if (command.length == 3) {
                    gameBuilder.append(dropItem(command[1] + " " + command[2]));
                    break;
                }
                if (command.length == 2) {
                    gameBuilder.append(dropItem(command[1]));
                    break;
                } else {
                    gameBuilder.append("\n \"Sorry, Dave. I can't drop that.\n");
                }
                break;
            case "talk":
                NPC character = new NPC(command[1]);
                gameBuilder.append(character.getDialogue());
                break;
            case "heal":
                if (currentLocation.contains("Medical Bay")) {
                    if (player.getHealth() >= 30) {
                        gameBuilder.append("\nYou're at max health.");
                    } else {
                        player.setHealth(player.getHealth() + 5);
                        gameBuilder.append("\nYour current health is: " + player.getHealth() + "HP. ");
                    }
                } else {
                    gameBuilder.append("\nSorry, Dave. You must be in the Medical Bay to heal.");
                }
                break;
            case "use":
                if (command.length == 2){
//                    healPlayer(command[1] + " " + command[2]);
                    if (command[1].equals("health kit")){
                        healPlayer();
                    }
                }
                break;
            case "fight":
                if (currentLocation.contains("Bar")) {
                    zombie.takeDamage(zombie.attack());
                    if (zombie.getHealth() <= 0) {
                        gameBuilder.append("\nYou dealt a fatal blow! The Zomburai is dead.");
                    } else {
                        gameBuilder.append("\nYou strike the Zomburai. It has " + zombie.getHealth() + "HP left.");
                    }
                } else {
                    gameBuilder.append("\nDave, stay focused. You can pick a fight later.");
                }
                break;
            case "help":
                gameBuilder.append(showInstructions());
                break;
            default:
                gameBuilder.append("Sorry, Dave. I can\'t do that.");

        }

        //update win/lose status
        checkPlayerHealth();
        checkPuzzleComplete();

        return gameBuilder.append(showStatus(currentLocation));
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
            response = catalog.get(object);
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

    private String examineRoom() {
        String description = "";
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
            description = (String) current.get("Description");
            return description + "\n";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return description + "\n";
    }

    private String headToNextRoom(String direction) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
            String next = (String) current.get(direction);
            if (current.containsKey(direction)) {
                currentLocation = next;
                return "You moved " + direction;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Can't go that way\n");
        }
        return "Can't go that way\n";
    }

    private String dropItem(String playerItem) {
        inventory.removeIf(item -> item.getName().equals(playerItem));
        // need to replace last item in list with empty item. removal of last item results in item being displayed in inventory container.
        if (inventory.size() <= 1) {
            Item emptyItem = new Item("", currentLocation);
            inventory.add(emptyItem);
        }
        return "You dropped the " + playerItem;
    }

    private void healPlayer() {
        StringBuilder builder = new StringBuilder();
        builder.append("Healing");
        Item healthKit = new Item("Health Kit", "Medical Bay");
        if (player.getInventory().contains(healthKit)) {
            System.out.println("Healing");
        } else {
            System.out.println("Missing a health kit bud.");
        }
    }

    private String pickUpItem(String thing) {
        try {
            JSONObject locations = (JSONObject) parser.parse(new FileReader("cfg/Locations.json"));
            JSONObject current = (JSONObject) locations.get(currentLocation);
            JSONArray itemsInRoom = (JSONArray) current.get("Item");

            if (itemsInRoom.contains(thing)) {
                Item itemToGet = new Item(thing, currentLocation);
                player.addToInventory(itemToGet);
                return "Placed " + thing + " in your inventory\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return "Item doesn't exist\n";
        }
        return thing + " doesn't exist\n";
    }
}