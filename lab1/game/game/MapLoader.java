package com.example.game.game;

import com.example.game.TempImage;
import com.example.game.entities.Enemy;
import com.example.game.entities.Entity;
import com.example.game.entities.Player;
import com.example.game.entities.Tile;
import com.example.game.helper.Coordinates;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class MapLoader {

  public MapLoader(File _file){
    tileSets = new HashMap<>();
    this.file = _file;
  }
  public MapData load() throws IOException {

    ArrayList<Tile> mapBGTiles = new ArrayList<>();
    ArrayList<Tile> mapTiles = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    Player player = null;
    ArrayList<Entity> nextLevelTriggers = new ArrayList<>();

    JSONObject mapJson = new JSONObject(Files.readString(file.toPath()));

    int spriteSize = mapJson.getInt("spriteSize");
    int tileSize = mapJson.getInt("tileSize");

    JSONArray tiles = mapJson.getJSONArray("tiles");
    for(int i = 0; i < tiles.length(); ++i){
      JSONObject tile = tiles.getJSONObject(i);
      int x = tile.getInt("x");
      int y = tile.getInt("y");
      TileTypes tileType = TileTypes.valueOf(tile.getString("tileType"));
      EntityTypes entityType = EntityTypes.valueOf(tile.getString("entityType"));

      int tileNumber = tile.getInt("tgi_tile_number");
      String fileName = tile.getString("tgi_file_name");

      // Load the sprite | If null get the tileSet and try again
      Image image = getImageFromTileSet(fileName, tileNumber);
      if(image == null){
        loadTileSet(fileName, spriteSize);
        image = getImageFromTileSet(fileName, tileNumber);
      }

      switch (tileType){
        case BACKGROUND -> {
          mapBGTiles.add(new Tile(
              new Coordinates(x * tileSize, y * tileSize),
              image,
              tileSize,
              Tile.TileTypes.BACKGROUND
          ));
        }
        case WALKABLE_TILE -> {
          mapTiles.add(new Tile(
              new Coordinates(x * tileSize, y * tileSize),
              image,
              tileSize,
              Tile.TileTypes.WALKABLE_TILE
          ));
        }
        case DANGEROUS_TILE -> {
          mapTiles.add(new Tile(
              new Coordinates(x * tileSize, y * tileSize),
              image,
              tileSize,
              Tile.TileTypes.DANGEROUS_TILE
          ));
        }
      }
      switch (entityType){
        case PLAYER -> {
          // The player is 0.01 smaller than the tile as to avoid spawn collisions with nearby tiles
          player = new Player(
              new Coordinates(x * tileSize, y * tileSize),
              TempImage.generateRandomImage(spriteSize),
              tileSize - 0.01
          );
        }
        case ENEMY -> {
          // The enemy is 0.01 smaller than the tile as to avoid spawn collisions with nearby tiles
          enemies.add(
              new Enemy(
                  new Coordinates(x * tileSize, y * tileSize),
                  TempImage.generateRandomImage(spriteSize),
                  tileSize - 0.01
              )
          );
        }
        case NEXT_LEVEL_TRIGGER -> {
          nextLevelTriggers.add(
              new Entity(
                  new Coordinates(x * tileSize, y * tileSize),
                  null,
                  tileSize
              )
          );
        }
      }

    }

    return new MapData(
        mapBGTiles,
        mapTiles,
        enemies,
        player,
        nextLevelTriggers
    );
  }

  private enum EntityTypes {
    NONE, PLAYER, ENEMY, NEXT_LEVEL_TRIGGER
  }
  private enum TileTypes {
    NONE, BACKGROUND, WALKABLE_TILE, DANGEROUS_TILE;
  }

  private final HashMap<String, Image[]> tileSets;
  private final File file;

  private void loadTileSet(String _fileName, int spriteSize){

    String filePath = "assets/tile_sets/" + _fileName;

    Image sampleImage = new Image(new File(filePath).toURI().toString());

    if(sampleImage.isError()){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Bad file selected");
      alert.setContentText("The file is not an image (or something else went wrong)");
      alert.showAndWait();
      return;
    }
    if(sampleImage.getHeight() % spriteSize != 0 || sampleImage.getWidth() % spriteSize != 0){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Bad file selected");
      alert.setContentText("The file must have width/height as multiples of 16");
      alert.showAndWait();
      return;
    }

    int rows = (int) (sampleImage.getHeight() / spriteSize);
    int cols = (int) (sampleImage.getWidth() / spriteSize);

    Image[] images = new Image[rows * cols];

    PixelReader pr = sampleImage.getPixelReader();
    for(int row = 0; row < rows; ++row) {
      for (int col = 0; col < cols; ++col) {
        images[row * cols + col] = new WritableImage(
            pr,
            col * spriteSize,
            row * spriteSize,
            spriteSize,
            spriteSize
        );
      }
    }

    tileSets.put(_fileName, images);
  }
  private Image getImageFromTileSet(String fileName, int tileNumber){

    Image[] imageArray = tileSets.get(fileName);

    if(imageArray == null){
      return null;
    }

    return imageArray[tileNumber];

  }

}
