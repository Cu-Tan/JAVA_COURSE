package com.example.student_registry.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Root extends Application {

  @Override
  public void start(Stage stage) {

    AdminPane adminPane = new AdminPane();

    Scene scene = new Scene(adminPane, 1280, 720);
    stage.setScene(scene);
    stage.show();

  }

}
