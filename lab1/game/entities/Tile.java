package com.example.game.entities;

import com.example.game.helper.Coordinates;
import javafx.scene.image.Image;

public class Tile extends Entity {

  public enum TileTypes {
    BACKGROUND, WALKABLE_TILE, DANGEROUS_TILE
  }

  TileTypes type;

  public Tile(Coordinates _coordinates, Image _img, double _size, TileTypes _type){
    super(_coordinates, _img, _size);
    this.type = _type;
  }

}
