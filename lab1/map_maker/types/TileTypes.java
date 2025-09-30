package com.example.map_maker.types;

import javafx.scene.paint.Color;

public enum TileTypes {
  NONE(null),
  BACKGROUND(Color.BLUE),
  WALKABLE_TILE(Color.GREEN),
  DANGEROUS_TILE(Color.RED);

  public Color color;
  TileTypes(Color _color){
    this.color = _color;
  }
}
