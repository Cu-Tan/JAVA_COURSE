package com.example.server.ui;

import com.example.server.logic.server.Server;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Root extends Application {

  @Override
  public void start(Stage stage) {

    Label portLabel = new Label("Port:");
    Text portText = new Text("UNKNOWN");
    HBox portLayout = new HBox(portLabel, portText);
    portLayout.setAlignment(Pos.CENTER);

    Button startServerButton = new Button("Start server");
    Button stopServerButton = new Button("Stop server");

    startServerButton.setOnAction( event -> {

      try{
        Server server = Server.getInstance();
        server.start();
        portText.setText( Integer.toString(server.getPort()) );
      }
      catch (Exception exception) {
        throw new RuntimeException(exception);
      }

    });
    stopServerButton.setOnAction( event -> {

      try{
        Server.getInstance().stop();
        portText.setText("UNKNOWN");
      }
      catch (Exception exception) {
        throw new RuntimeException(exception);
      }

    });

    VBox rootLayout = new VBox(portLayout, startServerButton, stopServerButton);
    rootLayout.setSpacing(16);
    rootLayout.setAlignment(Pos.CENTER);

    Scene rootScene = new Scene(rootLayout, 800, 600);

    rootLayout.prefWidthProperty().bind(rootScene.widthProperty());
    rootLayout.prefHeightProperty().bind(rootScene.heightProperty());

    stage.setScene(rootScene);
    stage.show();

  }

}
