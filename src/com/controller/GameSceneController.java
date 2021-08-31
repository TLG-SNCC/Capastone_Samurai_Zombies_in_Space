package com.controller;

import com.engine.GameEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable {

    @FXML
    private TextArea storyTextArea;

    @FXML
    private TextArea inventory;

    @FXML
    private TextField inputTextField;


    private final GameEngine gameEngine = new GameEngine();

    public GameSceneController() {
    }

    private TextField getInputTextField() {
        return inputTextField;
    }

    private void storyTextareaContainer() throws IOException {
        introStoryToTextarea();

    }

    /*
     * handles reading of user input from the Text Input Field in the Game Scene.
     * displays the read text in the uneditable TextArea field
     */
    public void handleTextFieldInput(ActionEvent event) {
        getInputTextField().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                storyTextArea.appendText(" > " + inputTextFieldString());
                storyTextArea.appendText(String.valueOf(gameEngine.runGameLoop(inputTextFieldString())));
                getInputTextField().clear();
                //TODO: dynamically set inventory
                //inventory.setText("Space Wrench");

            }
        });
    }

    private void populateInventoryTextArea() {

    }

    /*
     * initialized at start of game.
     * reads main story txt file and sends add it to the textarea
     */
    private void introStoryToTextarea() throws IOException {
        File file = new File("cfg/IntoStory.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while ((str = br.readLine()) != null) {
            storyTextArea.appendText(str + '\n');
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            introStoryToTextarea();
            appendInputToStoryTextarea(String.valueOf(gameEngine.status));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputTextFieldString() {
        return getInputTextField().getText();
    }

    private void appendInputToStoryTextarea(String strToDisplay) {
        storyTextArea.appendText(strToDisplay + '\n');
    }

}