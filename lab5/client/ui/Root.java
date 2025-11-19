package com.example.client.ui;

import com.example.client.logic.network.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Root extends Application {

  @Override
  public void start(Stage stage) {

    Scene rootScene = new Scene( new Pane(), 800, 600);
    ViewManager.getInstance().init(rootScene);

    stage.setScene(rootScene);
    stage.show();
    stage.setOnCloseRequest( e -> {
      Client.getInstance().disconnect();
    });

    // Try to connect to server
    if(Client.getInstance().connect()){
      ViewManager.getInstance().displayLoginView();
    }
    else {
      ViewManager.getInstance().displayConnectView();
    }
  }
}
