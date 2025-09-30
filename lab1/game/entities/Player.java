package com.example.game.entities;

import com.example.game.Animation;
import com.example.game.helper.Coordinates;
import com.example.game.game.GameConstants;
import com.example.game.game.PlayerInput;
import com.example.game.game.Time;
import com.example.game.collision.CollisionDirection;
import com.example.game.collision.CollisionInputData;
import com.example.game.collision.CollisionResult;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Player extends Entity {

  /*
    [CONSTRUCTORS]
  */

  public Player(Coordinates _coordinates, Image _img, double _size){
    super(_coordinates, _img, _size);

    loadAnimations();

    currentAnimation = idleAnimation;

    isAffectedByGravity = true;
    velocity = new Coordinates(0, 0);
  }

  /*
    [PUBLIC FIELDS]
  */

  public BiFunction<CollisionInputData, EntityType, CollisionResult[]> getCollisions;
  public Runnable gameKillPlayer;
  public Consumer<Entity> gameKillEnemy;
  public Runnable gameLevelComplete;

  /*
    [PUBLIC METHODS]
  */

  @Override
  public void update(){

    // Movement
    floatingCheck();
    setVelocity();
    move();

    // Animation
    setAnimationState();
    currentAnimation.update();
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private Animation currentAnimation;

  private Animation idleAnimation;
  private Animation runAnimation;
  private Animation jumpAnimation;

  private final double maxSpeed = 350;
  private final double maxFallSpeed = 750;
  private final double jumpVelocity = 600;
  private final double acceleration = 750;
  private final double deceleration = 1000;
  private Coordinates velocity;

  private boolean isAffectedByGravity = false;

  private enum AnimationState {
    IDLE, RUNNING, JUMPING
  }
  private AnimationState animationState = AnimationState.IDLE;

  private enum JumpState {
    GROUNDED, AIRBORNE
  }
  private JumpState jumpState = JumpState.GROUNDED;

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  private void setVelocity(){
    /*
      SET HORIZONTAL VELOCITY
     */
    if(PlayerInput.RIGHT_PRESSED){
      velocity.x += acceleration * Time.deltaTime;
      // Add additional deceleration if the player is moving in one direction but want to switch direction
      if(velocity.x < 0){
        velocity.x += deceleration * Time.deltaTime;
      }
    }
    else if(PlayerInput.LEFT_PRESSED){
      velocity.x -= acceleration * Time.deltaTime;
      // Add additional deceleration if the player is moving in one direction but want to switch direction
      if(velocity.x > 0){
        velocity.x -= deceleration * Time.deltaTime;
      }
    }
    else{
      if(velocity.x > 0){
        velocity.x -= deceleration * Time.deltaTime;
        if(velocity.x < 0){
          velocity.x = 0;
        }
      }
      else if (velocity.x < 0){
        velocity.x += deceleration * Time.deltaTime;
        if(velocity.x > 0){
          velocity.x = 0;
        }
      }
    }
    velocity.x = Math.clamp(velocity.x, -maxSpeed, maxSpeed);

    /*
      Handle JUMP
     */
    if(jumpState == JumpState.GROUNDED && PlayerInput.SPACE_PRESSED){
      velocity.y = jumpVelocity;
      jumpState = JumpState.AIRBORNE;
    }

    /*
      Handle gravity
     */
    if(isAffectedByGravity && jumpState == JumpState.AIRBORNE){
      velocity.y -= GameConstants.gravity * Time.deltaTime;
      if(velocity.y < -maxFallSpeed){
        velocity.y = -maxFallSpeed;
      }
    }
  }
  private void move(){

    if(velocity.x > 0){
      moveRight(Math.abs(velocity.x * Time.deltaTime));
    }
    else if (velocity.x < 0){
      moveLeft(Math.abs(velocity.x * Time.deltaTime));
    }

    if(velocity.y > 0){
      moveUp(Math.abs(velocity.y * Time.deltaTime));
    }
    else if (velocity.y < 0){
      moveDown(Math.abs(velocity.y * Time.deltaTime));
    }
  }
  private void floatingCheck(){
    CollisionResult[] collisions = getCollisions.apply(
      new CollisionInputData(
        this,
        CollisionDirection.DOWN,
        0.1
      ),
      EntityType.TILE
    );
    if(collisions.length == 0){
      jumpState = JumpState.AIRBORNE;
    }
  }

  // This function is used to detect player state and to set animations accordingly
  private void setAnimationState(){

    // Make the player face a certain direction
    if(velocity.x > 0){
      sprite.setScaleX(1);
    }
    else if (velocity.x < 0){
      sprite.setScaleX(-1);
    }

    switch (jumpState){
      case GROUNDED -> {
        if(Math.abs(velocity.x) > 0 && animationState != AnimationState.RUNNING){
          animationState = AnimationState.RUNNING;
          setAnimation(runAnimation);
        }
        else if (Math.abs(velocity.x) == 0 && animationState != AnimationState.IDLE){
          animationState = AnimationState.IDLE;
          setAnimation(idleAnimation);
        }
      }
      case AIRBORNE -> {
        if(Math.abs(velocity.y) > 0 && animationState != AnimationState.JUMPING){
          animationState = AnimationState.JUMPING;
          setAnimation(jumpAnimation);
        }
      }
    }

  }
  private void setAnimation(Animation _newAnimation){
    currentAnimation.reset();
    currentAnimation = _newAnimation;
  }

  private void moveUp(double _moveDistance){

    processNextLevelTriggerCollisions(_moveDistance, CollisionDirection.UP);
    precessEnemyCollisions(_moveDistance, CollisionDirection.UP);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.UP);
    this.coordinates.y -= _moveDistance;

  }
  private void moveRight(double _moveDistance){

    processNextLevelTriggerCollisions(_moveDistance, CollisionDirection.RIGHT);
    precessEnemyCollisions(_moveDistance, CollisionDirection.RIGHT);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.RIGHT);
    this.coordinates.x += _moveDistance;

  }
  private void moveLeft(double _moveDistance){

    processNextLevelTriggerCollisions(_moveDistance, CollisionDirection.LEFT);
    precessEnemyCollisions(_moveDistance, CollisionDirection.LEFT);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.LEFT);
    this.coordinates.x -= _moveDistance;

  }
  private void moveDown(double _moveDistance){

    processNextLevelTriggerCollisions(_moveDistance, CollisionDirection.DOWN);
    precessEnemyCollisions(_moveDistance, CollisionDirection.DOWN);

    _moveDistance = processTileCollisions(_moveDistance, CollisionDirection.DOWN);
    this.coordinates.y += _moveDistance;

  }

  private void processNextLevelTriggerCollisions(double _moveDistance, CollisionDirection _collisionDirection){

    CollisionResult[] nextLevelTriggerCollisions = getCollisions.apply(new CollisionInputData(
        this,
        _collisionDirection,
        _moveDistance
    ), EntityType.NEXT_LEVEL_TRIGGER );

    if(nextLevelTriggerCollisions.length != 0){
      gameLevelComplete.run();
    }

  }

  private void precessEnemyCollisions(double _moveDistance, CollisionDirection _collisionDirection){

    CollisionResult[] enemyCollisions = getCollisions.apply(new CollisionInputData(
        this,
        _collisionDirection,
        _moveDistance
    ), EntityType.ENEMY );

    for(CollisionResult enemyCollision: enemyCollisions){
      processEnemyCollision(enemyCollision);
    }
  }
  private void processEnemyCollision(CollisionResult _colResult){
    switch (_colResult.collisionDirection){
      case DOWN -> {
        Entity enemyToKill = null;
        // Both rays interact with the same object so it does not matter which ray entity to choose
        if(_colResult.ray1Collision != null){
          enemyToKill = _colResult.ray1Collision.collisionEntity;
        }
        else if (_colResult.ray2Collision != null){
          enemyToKill = _colResult.ray2Collision.collisionEntity;
        }
        gameKillEnemy.accept(enemyToKill);
      }
      case RIGHT, LEFT, UP -> {
        gameKillPlayer.run();
      }
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



    if(_colResult.collisionDirection == CollisionDirection.UP){
      velocity.y = 0;
    }
    else if (_colResult.collisionDirection == CollisionDirection.DOWN){
      velocity.y = 0;
      jumpState = JumpState.GROUNDED;
    }
    if(_colResult.collisionDirection == CollisionDirection.RIGHT || _colResult.collisionDirection == CollisionDirection.LEFT){
      velocity.x = 0;
    }

    double colDistance = 0;

    // It does not matter which ray's distance you take since if both rays hit they WILL be the same distance
    if(_colResult.ray1Collision != null){
      colDistance = _colResult.ray1Collision.collisionDistance;
    }
    else if (_colResult.ray2Collision != null){
      colDistance = _colResult.ray2Collision.collisionDistance;
    }

    // Need to return with 0.01 less because otherwise the player will be INSIDE the tile it collided with
    return colDistance - 0.01;
  }

  private void loadAnimations(){

    this.idleAnimation = new Animation(
        loadAnimationFrames("file:assets/player/player_idle.png"),
        this.sprite,
        0.5,
        true
    );

    this.runAnimation = new Animation(
        loadAnimationFrames("file:assets/player/player_run.png"),
        this.sprite,
        0.5,
        true
    );

    this.jumpAnimation = new Animation(
        loadAnimationFrames("file:assets/player/player_jump.png"),
        this.sprite,
        1,
        false
    );

  }
  private Image[] loadAnimationFrames(String _uri){
    Image idleAnimationImage = new Image(_uri);
    PixelReader px = idleAnimationImage.getPixelReader();

    ArrayList<Image> idleAnimationFrames = new ArrayList<>();

    int count = (int)(idleAnimationImage.getWidth() / 16);
    for(int i = 0; i < count; ++i){
      idleAnimationFrames.add(
          new WritableImage(
              px,
              i * 16,
              0,
              16,
              16
          )
      );
    }
    return idleAnimationFrames.toArray(new Image[0]);

  }

}
