package com.example.game.entities;

import com.example.game.helper.Coordinates;
import com.example.game.game.Time;
import com.example.game.collision.CollisionDirection;
import com.example.game.collision.CollisionInputData;
import com.example.game.collision.CollisionResult;
import javafx.scene.image.Image;

import java.util.function.BiFunction;

public class Enemy extends Entity {

  /*
    [CONSTRUCTORS]
  */

  public Enemy(Coordinates _coordinates, Image _sprite, double _size){
    super(_coordinates, _sprite, _size);
    // Initiate every enemy to move left
    velocity = new Coordinates(-speed, 0);
  }

  /*
    [PUBLIC FIELDS]
  */

  public BiFunction<CollisionInputData, EntityType, CollisionResult[]> getCollisions;
  public Runnable gameKillPlayer;

  /*
    [PUBLIC METHODS]
  */

  @Override
  public void update(){
    floatCheck();
    move();
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private final double speed = 125;
  private final Coordinates velocity;

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  private void move(){
    if(velocity.x > 0){
      moveRight(Math.abs(velocity.x * Time.deltaTime));
    }
    else if (velocity.x < 0){
      moveLeft(Math.abs(velocity.x * Time.deltaTime));
    }
  }

  private void moveRight(double _moveDistance){

    processPlayerCollision(_moveDistance, CollisionDirection.RIGHT);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.RIGHT);
    this.coordinates.x += _moveDistance;
  }
  private void moveLeft(double _moveDistance){

    processPlayerCollision(_moveDistance, CollisionDirection.LEFT);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.LEFT);
    this.coordinates.x -= _moveDistance;
  }

  /**
   * Checks if the enemy is not fully on the ground. If not changes movement direction
   */
  private void floatCheck(){
    CollisionResult[] tileCollisions = getCollisions.apply(new CollisionInputData(
        this,
        CollisionDirection.DOWN,
        0.1
    ), EntityType.TILE );

    boolean ray1HitGround = false;
    boolean ray2HitGround = false;

    for(CollisionResult colResult: tileCollisions){
      if(colResult.ray1Collision != null){
        ray1HitGround = true;
      }
      if(colResult.ray2Collision != null){
        ray2HitGround = true;
      }
    }

    // Check if bottom-right corner is not on the ground if so move to left
    if(!ray1HitGround){
      velocity.x = -speed;
    }
    // Check if bottom-left corner is not on the ground if so move to right
    else if (!ray2HitGround){
      velocity.x = speed;
    }

  }

  private void processPlayerCollision(double _moveDistance, CollisionDirection _collisionDirection){

    CollisionResult[] playerCollision = getCollisions.apply(new CollisionInputData(
        this,
        _collisionDirection,
        _moveDistance
    ), EntityType.PLAYER );

    if(playerCollision.length != 0){
      gameKillPlayer.run();
    }

  }

  private double processTileCollisions(double _moveDistance, CollisionDirection _collisionDirection){

    CollisionResult[] tileCollisions = getCollisions.apply(new CollisionInputData(
        this,
        _collisionDirection,
        _moveDistance
    ), EntityType.TILE );

    for(CollisionResult colResult: tileCollisions){
      _moveDistance = Math.min(processTileCollision(colResult), _moveDistance);
    }

    return _moveDistance;
  }
  private double processTileCollision(CollisionResult _colResult){

    // If the enemy hits a wall reverse it's walking direction
    velocity.x = -velocity.x;

    double colDistance = 0;

    // It does not matter which ray's distance you take since if both rays hit they WILL be the same distance
    if(_colResult.ray1Collision != null){
      colDistance = _colResult.ray1Collision.collisionDistance;
    }
    else if (_colResult.ray2Collision != null){
      colDistance = _colResult.ray2Collision.collisionDistance;
    }

    // Need to return with 0.01 less because otherwise the entity will be INSIDE the tile it collided with
    return colDistance - 0.01;
  }
}
