package com.example.client.ui.views;

import com.example.client.logic.network.Client;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {

  public LoginView(){

    Label usernameLabel = new Label("Username:");
    TextField usernameField = new TextField();
    HBox usernameLayout = new HBox(usernameLabel, usernameField);
    usernameLayout.setAlignment(Pos.CENTER);

    Button connectButton = new Button("Login");

    connectButton.setOnAction( e -> {

      Client.getInstance().login(usernameField.getText());

    });

    this.getChildren().addAll(usernameLayout, connectButton);
    this.setAlignment(Pos.CENTER);

  }

}
