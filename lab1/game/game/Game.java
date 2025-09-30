package com.example.game.game;

import com.example.game.collision.CollisionDirection;
import com.example.game.collision.CollisionInputData;
import com.example.game.collision.CollisionResult;
import com.example.game.entities.Enemy;
import com.example.game.entities.Entity;
import com.example.game.entities.EntityType;
import com.example.game.entities.Tile;
import com.example.game.helper.CTMath;
import com.example.game.helper.Coordinates;
import com.example.game.helper.Line;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class Game {

  /*
    [CONSTRUCTORS]
  */

  /**
   * Creates game class and instantly starts the game loop
   */
  public Game(Scene _window, Pane _world){

    this.window = _window;
    this.world = _world;

    setup();
  }

  /*
    [PUBLIC FIELDS]
  */

  public Scene window;
  public Pane world;

  /*
    [PUBLIC METHODS]
  */

  public void killPlayer(){
    gameOver();
  }
  public void killEnemy(Entity _enemy){
    mapData.enemies.remove(_enemy);
    world.getChildren().remove(_enemy.sprite);
  }

  /**
   * @return all the collisions with a certain entity type
   */
  public CollisionResult[] projectedCollisionWithSelected(CollisionInputData _cid, EntityType _entityType){

    ArrayList<CollisionResult> collisions = new ArrayList<>();

    switch (_entityType){
      case TILE -> {
        for(Tile tile: mapData.tiles){
          CollisionResult colResult = projectedCollision(
              _cid.sourceEntity,
              tile,
              _cid.moveDirection,
              _cid.moveDistance
          );
          if(!(colResult.ray1Collision == null && colResult.ray2Collision == null)){
            collisions.add(colResult);
          }

        }
      }
      case ENEMY -> {
        for(Enemy enemy: mapData.enemies){
          CollisionResult colResult = projectedCollision(
              _cid.sourceEntity,
              enemy,
              _cid.moveDirection,
              _cid.moveDistance
          );
          if(!(colResult.ray1Collision == null && colResult.ray2Collision == null)){
            collisions.add(colResult);
          }
        }
      }
      case PLAYER -> {
        CollisionResult colResult = projectedCollision(
            _cid.sourceEntity,
            mapData.player,
            _cid.moveDirection,
            _cid.moveDistance
        );
        if(!(colResult.ray1Collision == null && colResult.ray2Collision == null)){
          collisions.add(colResult);
        }
      }
      case NEXT_LEVEL_TRIGGER -> {
        for(Entity nextLevelTrigger: mapData.nextLevelTriggers){
          CollisionResult colResult = projectedCollision(
              _cid.sourceEntity,
              nextLevelTrigger,
              _cid.moveDirection,
              _cid.moveDistance
          );
          if(!(colResult.ray1Collision == null && colResult.ray2Collision == null)){
            collisions.add(colResult);
          }
        }
      }
    }

    return collisions.toArray(new CollisionResult[0]);
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private final AtomicBoolean endGameLoop = new AtomicBoolean(false);

  private int currentLevel = 1;
  private static final int MAX_LEVELS = 4;

  private MapData mapData;

  private final GameLoop gameLoop = new GameLoop();

  private class GameLoop extends AnimationTimer {

    private long lastTime = 0;

    public void reset(){
      lastTime = 0;
    }

    @Override
    public void handle(long now) {

      if(lastTime == 0){
        lastTime = now;
        return;
      }

      Time.deltaTime = (now - lastTime) / 1_000_000_000.0;
      lastTime = now;

      update();
      render();

    }

  }

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  /**
   * @return information about the collision with an object
   */
  private CollisionResult projectedCollision(
      Entity _source,
      Entity _other,
      CollisionDirection _direction,
      double _distance
  ){
    /* When traveling in any direction you only need to check collisions on the _other colliders opposite wall
      Example:
      Traveling up only requires to check your top wall with _other bottom wall
      Traveling down (bottom -> top)
      etc.
     */

    // Create 2 lines from each wall point on moving object

    Line ray1 = null;
    Line ray2 = null;
    Line otherWall = null;

    switch (_direction){
      case UP -> {

        ray1 = new Line(
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y - _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y - _source.size / 2 - _distance
            )
        );

        ray2 = new Line(
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y - _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y - _source.size / 2 - _distance
            )
        );

        otherWall = new Line(
            new Coordinates(
                _other.coordinates.x + _other.size / 2,
                _other.coordinates.y + _other.size / 2
            ),
            new Coordinates(
                _other.coordinates.x - _other.size / 2,
                _other.coordinates.y + _other.size / 2
            )
        );
      }
      case RIGHT -> {
        ray1 = new Line(
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y - _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x + _source.size / 2 + _distance,
                _source.coordinates.y - _source.size / 2
            )
        );

        ray2 = new Line(
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y + _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x + _source.size / 2 + _distance,
                _source.coordinates.y + _source.size / 2
            )
        );

        otherWall = new Line(
            new Coordinates(
                _other.coordinates.x - _other.size / 2,
                _other.coordinates.y - _other.size / 2
            ),
            new Coordinates(
                _other.coordinates.x - _other.size / 2,
                _other.coordinates.y + _other.size / 2
            )
        );
      }
      case DOWN -> {

        ray1 = new Line(
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y + _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x + _source.size / 2,
                _source.coordinates.y + _source.size / 2 + _distance
            )
        );

        ray2 = new Line(
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y + _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y + _source.size / 2 + _distance
            )
        );

        otherWall = new Line(
            new Coordinates(
                _other.coordinates.x + _other.size / 2,
                _other.coordinates.y - _other.size / 2
            ),
            new Coordinates(
                _other.coordinates.x - _other.size / 2,
                _other.coordinates.y - _other.size / 2
            )
        );

      }
      case LEFT -> {

        ray1 = new Line(
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y - _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x - _source.size / 2 - _distance,
                _source.coordinates.y - _source.size / 2
            )
        );

        ray2 = new Line(
            new Coordinates(
                _source.coordinates.x - _source.size / 2,
                _source.coordinates.y + _source.size / 2
            ),
            new Coordinates(
                _source.coordinates.x - _source.size / 2 - _distance,
                _source.coordinates.y + _source.size / 2
            )
        );

        otherWall = new Line(
            new Coordinates(
                _other.coordinates.x + _other.size / 2,
                _other.coordinates.y + _other.size / 2
            ),
            new Coordinates(
                _other.coordinates.x + _other.size / 2,
                _other.coordinates.y - _other.size / 2
            )
        );

      }
    }

    Coordinates col1 = CTMath.lineIntersection(ray1, otherWall);
    double distance1 = (col1 != null) ? new Line(ray1.start, col1).distance() : _distance;
    Coordinates col2 = CTMath.lineIntersection(ray2, otherWall);
    double distance2 = (col2 != null) ? new Line(ray2.start, col2).distance() : _distance;

    return new CollisionResult(
        (col1 == null) ? null : new CollisionResult.RayCollisionResult(
            distance1,
            _other
        ),
        (col2 == null) ? null : new CollisionResult.RayCollisionResult(
            distance2,
            _other
        ),
        _direction
    );
  }

  /**
   * Initial setup when game class is created
   */
  private void setup(){

    loadLevel(currentLevel);

    initInput();

    gameLoop.start();
  }

  /**
   * Updates all entities
   */
  private void update(){

    mapData.player.update();
    for(Enemy enemy: mapData.enemies){
      enemy.update();
    }

  }

  /**
   * Renders all entities to the screen
   */
  private void render(){

    Coordinates camOffset = new Coordinates(
      window.getWidth() / 2 - mapData.player.coordinates.x,
      window.getHeight() / 2 - mapData.player.coordinates.y
    );

    for(Tile bgTile: mapData.bgTiles){
      bgTile.render(camOffset);
    }
    for(Tile tile : mapData.tiles){
      tile.render(camOffset);
    }
    for(Enemy enemy: mapData.enemies){
      enemy.render(camOffset);
    }
    mapData.player.render(camOffset);
  }

  private void initInput(){

    window.addEventHandler(KeyEvent.ANY, PlayerInput::handle);

  }

  /**
   * Called when a level is complete. If there are more levels available it calls "nextLevel" if not "gameWon"
   */
  private void levelComplete(){

    // If any other event has already started ending the game loop exit
    if(endGameLoop.get()){
      return;
    }
    endGameLoop.set(true);

    gameLoop.stop();

    if (currentLevel == MAX_LEVELS) {
      gameWon();
    } else {
      nextLevel();
    }
  }

  /**
   * Creates a pop up for the player to select to replay the current level or play next level
   */
  private void nextLevel(){
    Dialog<Void> nextLevelPopup = new Dialog<>();
    nextLevelPopup.setTitle("LEVEL COMPLETE");

    ButtonType nextLevelButtonType = new ButtonType("Next level", ButtonBar.ButtonData.OK_DONE);
    ButtonType replayButtonType = new ButtonType("Replay level", ButtonBar.ButtonData.CANCEL_CLOSE);
    nextLevelPopup.getDialogPane().getButtonTypes().addAll(replayButtonType, nextLevelButtonType);

    Button nextLevelButton = (Button) nextLevelPopup.getDialogPane().lookupButton(nextLevelButtonType);
    nextLevelButton.setOnAction( e -> {
      world.getChildren().clear();
      currentLevel++;
      loadLevel(currentLevel);
      startGameLoop();
    });

    Button replayButton = (Button) nextLevelPopup.getDialogPane().lookupButton(replayButtonType);
    replayButton.setOnAction( e -> {
      world.getChildren().clear();
      loadLevel(currentLevel);
      startGameLoop();
    });

    Platform.runLater(nextLevelPopup::showAndWait);
  }

  /**
   * Creates a pop up for the player to select to replay the game or exit
   */
  private void gameWon(){
    Dialog<Void> gameWonPopup = new Dialog<>();
    gameWonPopup.setTitle("GAME WON");

    ButtonType replayButtonType = new ButtonType("Replay from lvl 1", ButtonBar.ButtonData.OK_DONE);
    ButtonType exitButtonType = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

    gameWonPopup.getDialogPane().getButtonTypes().addAll(exitButtonType, replayButtonType);

    Button exitButton = (Button) gameWonPopup.getDialogPane().lookupButton(exitButtonType);
    exitButton.setOnAction( e -> {
      System.exit(0);

    });

    Button replayButton = (Button) gameWonPopup.getDialogPane().lookupButton(replayButtonType);
    replayButton.setOnAction( e -> {
      world.getChildren().clear();
      currentLevel = 1;
      loadLevel(currentLevel);
      startGameLoop();
    });

    Platform.runLater(gameWonPopup::showAndWait);
  }

  /**
   * Creates a pop up for the player to select to exit the game or replay current level
   */
  private void gameOver(){

    // If any other event has already started ending the game loop exit
    if(endGameLoop.get()){
      return;
    }
    endGameLoop.set(true);

    gameLoop.stop();

    Dialog<Void> gameOverPopup = new Dialog<>();
    gameOverPopup.setTitle("GAMEOVER");

    ButtonType restartButtonType = new ButtonType("Restart", ButtonBar.ButtonData.OK_DONE);
    ButtonType quitButtonType = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
    gameOverPopup.getDialogPane().getButtonTypes().addAll(quitButtonType, restartButtonType);

    Button restartButton = (Button) gameOverPopup.getDialogPane().lookupButton(restartButtonType);
    restartButton.setOnAction( e -> {
      world.getChildren().clear();
      loadLevel(currentLevel);
      startGameLoop();
    });

    Button quitButton = (Button) gameOverPopup.getDialogPane().lookupButton(quitButtonType);
    quitButton.setOnAction( e -> {
      System.exit(0);
    });

    Platform.runLater(gameOverPopup::showAndWait);
  }

  private void loadLevel(int _level){

    // Try to load level file
    File file = new File("assets/levels/lvl" + _level + ".json");
    MapLoader mapLoader = new MapLoader(file);
    try{
      mapData = mapLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    mapData.player.getCollisions = this::projectedCollisionWithSelected;
    mapData.player.gameKillEnemy = this::killEnemy;
    mapData.player.gameKillPlayer = this::killPlayer;
    mapData.player.gameLevelComplete = this::levelComplete;

    for(Tile bgTile: mapData.bgTiles){
      world.getChildren().add(bgTile.sprite);
    }
    for(Tile tile: mapData.tiles){
      world.getChildren().add(tile.sprite);
    }
    for(Enemy enemy: mapData.enemies){
      enemy.getCollisions = this::projectedCollisionWithSelected;
      enemy.gameKillPlayer = this::killPlayer;
      world.getChildren().add(enemy.sprite);
    }
    world.getChildren().add(mapData.player.sprite);

  }

  private void startGameLoop(){
    endGameLoop.set(false);
    PlayerInput.resetInput();
    gameLoop.reset();
    gameLoop.start();
  }
}
