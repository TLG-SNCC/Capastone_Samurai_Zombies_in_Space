module Samurai.Zombies.In.Space {

    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires gson;
    requires java.sql;

    opens com.controller to javafx.fxml;

    exports com.client;
}