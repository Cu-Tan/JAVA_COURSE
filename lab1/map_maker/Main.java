package com.example.map_maker;

import com.example.map_maker.grids.SampleGrid;
import com.example.map_maker.map.MapLoader;
import com.example.map_maker.map.MapSaver;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

/**
 * @author Karolis Ribačonka Informatika 2 kursas 2 grupė 2 pogrupis
 */
public class Main extends Application {
  @Override
  public void start(Stage stage) throws IOException {

    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 1200, 800);

    /// Menu area (Left)
    VBox menu = new VBox();
    menu.setFillWidth(true);
    menu.setStyle(
        "-fx-background-color: #ccc;"
    );
    menu.setAlignment(Pos.CENTER);

    menu.setSpacing(5);

    MapPainter mapPainter = new MapPainter();
    MapPainterMenu mapPainterMenu = new MapPainterMenu(mapPainter);


    ///  Button creation and insertion to menu
    Button loadImageButton = new Button("Load sample image");
    Button loadMapButton = new Button("Load map");
    Button saveMapButton = new Button("Save map");
    Region spacer = new Region();
    spacer.setMinHeight(32);
    spacer.setPrefHeight(32);
    spacer.setMaxHeight(32);
    menu.getChildren().addAll(mapPainterMenu, spacer, loadImageButton, loadMapButton, saveMapButton);
    root.setLeft(menu);

    /// Right half of remaining screen
    VBox rightArea = new VBox();
    rightArea.setSpacing(16);

    /// Bottom half
    MapPanel mapPanel = new MapPanel(rightArea.heightProperty().divide(2), rightArea.widthProperty(), mapPainter);
    mapPanel.mapGrid.prefWidthProperty().bind(rightArea.widthProperty());

    /// Top half
    SampleGrid sampleGrid = new SampleGrid(0, 0, 16, 25f);
    sampleGrid.prefHeightProperty().bind(rightArea.heightProperty().divide(2));
    sampleGrid.prefWidthProperty().bind(rightArea.widthProperty());
    sampleGrid.setOnTileClick( _tgi -> {
      mapPainter.tgi = _tgi;
    });

    loadImageButton.setOnAction( e -> {

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select image");

      File file = fileChooser.showOpenDialog(stage);
      if(file != null){
        if(sampleGrid.setSampleImage(file.toURI().toString())){
          mapPainter.tgi = null;
        }
      }

    });

    loadMapButton.setOnAction( e -> {

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Load map");
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

      File file = fileChooser.showOpenDialog(stage);
      if(file == null) return;

      MapLoader mapLoader = new MapLoader(file);

      try {
        mapPanel.mapGrid.setMap(mapLoader.load());
        mapPanel.rowsField.setText(mapPanel.mapGrid.getMap().height + "");
        mapPanel.colsField.setText(mapPanel.mapGrid.getMap().width + "");
      }
      catch (IOException ex) {
        throw new RuntimeException(ex);
      }

    });

    saveMapButton.setOnAction( e -> {

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save map");
      fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

      File file = fileChooser.showSaveDialog(stage);

      MapSaver mapSaver = new MapSaver(file, mapPanel.mapGrid.getMap());

      try {
        mapSaver.save();
      }
      catch (IOException ex) {
        throw new RuntimeException(ex);
      }

    });




    rightArea.getChildren().addAll(sampleGrid, mapPanel);
    root.setCenter(rightArea);

    stage.setTitle("Map editor");
    stage.setScene(scene);
    stage.show();
  }
}
