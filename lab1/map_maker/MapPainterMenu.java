package com.example.map_maker;

import com.example.map_maker.types.EntityTypes;
import com.example.map_maker.types.TileTypes;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A menu used for editing map painter settings
 */
public class MapPainterMenu extends VBox {

  MapPainterMenu(MapPainter _mapPainter){

    // Add tile type dropdown and set events
    VBox tileVBox = new VBox();
    Label tileTypeLabel = new Label("Tile type");
    ComboBox<TileTypes> tileTypeDropdown = new ComboBox<>();
    tileTypeDropdown.getItems().addAll(TileTypes.values());
    tileTypeDropdown.setValue(TileTypes.NONE);
    tileTypeDropdown.setDisable(true);
    tileVBox.getChildren().addAll(tileTypeLabel, tileTypeDropdown);

    tileTypeDropdown.setOnAction( e -> {
      _mapPainter.tileType = tileTypeDropdown.getValue();
    });

    // Add entity type dropdown and set events
    VBox entityVBox = new VBox();
    Label entityTypeLabel = new Label("Entity type");
    ComboBox<EntityTypes> entityTypeDropdown = new ComboBox<>();
    entityTypeDropdown.getItems().addAll(EntityTypes.values());
    entityTypeDropdown.setValue(EntityTypes.NONE);
    entityTypeDropdown.setDisable(true);
    entityVBox.getChildren().addAll(entityTypeLabel, entityTypeDropdown);

    entityTypeDropdown.setOnAction( e -> {
      _mapPainter.entityType = entityTypeDropdown.getValue();
    });

    // Add modes dropdown and set events
    VBox modesVBox = new VBox();
    Label modesLabel = new Label("Painter modes");
    ComboBox<MapPainter.Modes> modesDropdown = new ComboBox<>();
    modesDropdown.getItems().addAll(MapPainter.Modes.values());
    modesDropdown.setValue(MapPainter.Modes.NONE);
    modesVBox.getChildren().addAll(modesLabel, modesDropdown);

    modesDropdown.setOnAction( e -> {
      switch (modesDropdown.getValue()){
        case NONE -> {
          _mapPainter.mode = MapPainter.Modes.NONE;
          _mapPainter.tileType = TileTypes.NONE;
          _mapPainter.entityType = EntityTypes.NONE;

          tileTypeDropdown.setValue(TileTypes.NONE);
          entityTypeDropdown.setValue(EntityTypes.NONE);

          tileTypeDropdown.setDisable(true);
          entityTypeDropdown.setDisable(true);
        }
        case TILE -> {
          _mapPainter.mode = MapPainter.Modes.TILE;
          _mapPainter.tileType = TileTypes.NONE;
          _mapPainter.entityType = EntityTypes.NONE;

          tileTypeDropdown.setValue(TileTypes.NONE);
          entityTypeDropdown.setValue(EntityTypes.NONE);

          tileTypeDropdown.setDisable(false);
          entityTypeDropdown.setDisable(true);
        }
        case ENTITY -> {
          _mapPainter.mode = MapPainter.Modes.ENTITY;
          _mapPainter.tileType = TileTypes.NONE;
          _mapPainter.entityType = EntityTypes.NONE;

          tileTypeDropdown.setValue(TileTypes.NONE);
          entityTypeDropdown.setValue(EntityTypes.NONE);

          tileTypeDropdown.setDisable(true);
          entityTypeDropdown.setDisable(false);
        }
      }
    });

    modesVBox.setAlignment(Pos.CENTER);
    tileVBox.setAlignment(Pos.CENTER);
    entityVBox.setAlignment(Pos.CENTER);

    this.setSpacing(4);
    this.getChildren().addAll(modesVBox, tileVBox, entityVBox);
  }

}
