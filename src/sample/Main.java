package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Samurai Zombies in Space");
        primaryStage.setScene(new Scene(root, 800, 775));
        primaryStage.show();
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


    public static void main(String[] args) {
        launch(args);
    }
}