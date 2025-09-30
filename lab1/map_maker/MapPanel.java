package com.example.map_maker;

import com.example.map_maker.grids.MapGrid;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Hold the map grid and setting to edit map grid display / size
 */
public class MapPanel extends Pane {

  public final MapGrid mapGrid;

  public TextField colsField;
  public TextField rowsField;

  MapPanel(DoubleBinding prefHeight, ReadOnlyDoubleProperty prefWidth, MapPainter _mapPainter){

    this.minHeightProperty().bind(prefHeight);
    this.prefHeightProperty().bind(prefHeight);
    this.maxHeightProperty().bind(prefHeight);
    this.prefWidthProperty().bind(prefWidth);

    mapGrid = new MapGrid(0, 0, 16, 16, _mapPainter);

    VBox rowsBox = new VBox();
    Text rowsText = new Text("Rows:");
    rowsField = new TextField("0");
    rowsField.setPrefWidth(64);
    rowsField.textProperty().addListener((obs, oldValue, newValue) -> {
      if(!newValue.matches("\\d*")){
        rowsField.setText(newValue.replace("[^\\d]", ""));
      }
    });
    rowsBox.getChildren().addAll(rowsText, rowsField);

    VBox colsBox = new VBox();
    Text colsText = new Text("Columns:");
    colsField = new TextField("0");
    colsField.setPrefWidth(64);
    colsField.textProperty().addListener((obs, oldValue, newValue) -> {
      if(!newValue.matches("\\d*")){
        colsField.setText(newValue.replace("[^\\d]", ""));
      }
    });
    colsBox.getChildren().addAll(colsText, colsField);

    Button resizeButton = new Button();
    resizeButton.setText("Resize");
    resizeButton.setOnAction( e -> {
      mapGrid.setRowsCols(Integer.parseInt(rowsField.getText()), Integer.parseInt(colsField.getText()));
    });

    CheckBox showTileTypeHint = new CheckBox("Show tile type hints");
    showTileTypeHint.setSelected(false);
    showTileTypeHint.setOnAction( e -> {
      mapGrid.showTileTypeHint = showTileTypeHint.isSelected();
      mapGrid.draw();
    });

    CheckBox showEntitiesHint = new CheckBox("Show entity hints");
    showEntitiesHint.setSelected(false);
    showEntitiesHint.setOnAction( e -> {
      mapGrid.showEntitiesHint = showEntitiesHint.isSelected();
      mapGrid.draw();
    });

    VBox hintVBox = new VBox(showTileTypeHint, showEntitiesHint);
    hintVBox.setAlignment(Pos.BOTTOM_LEFT);

    Region spacer = new Region();
    spacer.setMinWidth(32);
    spacer.setPrefWidth(32);
    spacer.setMaxWidth(32);

    HBox menuBar = new HBox();
    menuBar.getChildren().addAll(rowsBox, colsBox, resizeButton, spacer, hintVBox);
    menuBar.setAlignment(Pos.BOTTOM_LEFT);

    VBox allItems = new VBox();

    mapGrid.prefHeightProperty().bind(this.heightProperty().subtract(menuBar.heightProperty()));

    allItems.getChildren().addAll(menuBar, mapGrid);

    this.getChildren().add(allItems);
  }

}
