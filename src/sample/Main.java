package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Samurai Zombies in Space");
        primaryStage.setScene(new Scene(root, 800, 775));
        primaryStage.show();
    }



    public static void main(String[] args) {
//        launch(args);

        // Instantiate Scanner class to get user input
        Scanner scanner = new Scanner(System.in);

        // Setting up the different rooms in the spaceship
        HashMap<String, HashMap<String, String>> spaceship = new HashMap<String, HashMap<String, String>>();

        //Create a landing dock room with a door to go north
        HashMap<String, String> landingDock = new HashMap<>();
        landingDock.put("north", "hall");

        //Create a hall room with a door to go south
        HashMap<String, String> hall = new HashMap<>();
        hall.put("south", "landing dock");

        //Adding the two rooms to the spaceship object
        spaceship.put("landing dock", landingDock);
        spaceship.put("hall", hall);

        boolean game = true;
        String command;
        String currentLocation = "landing dock";


        while (game) {
            System.out.println("You are currently in the " + currentLocation);
            System.out.println();
            System.out.println("Where do you want to go?");
            System.out.println("Commands:");
            System.out.println("go north");
            System.out.println("go south");
            System.out.println("q to quit");
            System.out.println();

            command = scanner.nextLine();
            System.out.println("Command is: " + command);

            if (command.equals("q")){
                game = false;
                System.out.println("Now leaving the game");
            }

            if (command.equals("go north")){
                currentLocation = spaceship.get(currentLocation).get("north");
            }

            if (command.equals("go south")){
                currentLocation = spaceship.get(currentLocation).get("south");
            }

        }

        System.exit(0);
    }
}