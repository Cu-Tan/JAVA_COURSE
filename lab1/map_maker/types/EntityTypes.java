package com.example.map_maker.types;

import javafx.scene.paint.Color;

public enum EntityTypes {
  NONE(null),
  PLAYER(Color.BLUE),
  ENEMY(Color.RED),
  NEXT_LEVEL_TRIGGER(Color.VIOLET);

  public Color color;
  EntityTypes(Color _color){
    this.color = _color;
  }
}
