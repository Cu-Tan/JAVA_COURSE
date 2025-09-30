package com.example.game.collision;

import com.example.game.entities.Entity;

public class CollisionResult {

  public static class RayCollisionResult{

    public double collisionDistance;
    public Entity collisionEntity;

    public RayCollisionResult(
        double _collisionDistance,
        Entity _collisionEntity
    ) {
      this.collisionDistance = _collisionDistance;
      this.collisionEntity = _collisionEntity;
    }
  }

  public RayCollisionResult ray1Collision;
  public RayCollisionResult ray2Collision;

  public CollisionDirection collisionDirection;

  public CollisionResult(
    RayCollisionResult _ray1Collision,
    RayCollisionResult _ray2Collision,
    CollisionDirection _collisionDirection
  ){
    this.ray1Collision = _ray1Collision;
    this.ray2Collision = _ray2Collision;
    this.collisionDirection = _collisionDirection;
  }

}
