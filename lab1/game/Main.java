package com.example.game;

import com.example.game.game.Game;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Karolis Ribačonka Informatika 2 kursas 2 grupė 2 pogrupis
 */

public class Main extends Application {

  @Override
  public void start(Stage stage) {

    Pane world = new Pane();

    Pane root = new Pane(world);
    root.setBackground(new Background(
        new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
    ));

    Scene scene = new Scene(root, 1280, 720);

    Game game = new Game(scene, world);

    stage.setScene(scene);
    stage.show();

  }
}
