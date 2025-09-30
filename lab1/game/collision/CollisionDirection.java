package com.example.game.collision;

public enum CollisionDirection {
  UP(0, 1),
  RIGHT(1, 0),
  LEFT(-1, 0),
  DOWN(0, -1);

  final int x;
  final int y;
  CollisionDirection(int _x, int _y){
    this.x = _x;
    this.y = _y;
  }

}
