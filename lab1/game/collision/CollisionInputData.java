package com.example.game.collision;

import com.example.game.entities.Entity;

public class CollisionInputData {

  public Entity sourceEntity;
  public CollisionDirection moveDirection;
  public double moveDistance;

  public CollisionInputData(Entity _sourceEntity, CollisionDirection _moveDirection, double _moveDistance){
    this.sourceEntity = _sourceEntity;
    this.moveDirection = _moveDirection;
    this.moveDistance = _moveDistance;
  }

}
