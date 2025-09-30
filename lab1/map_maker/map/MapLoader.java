package com.example.map_maker.map;

import com.example.map_maker.TileGraphicInfo;
import com.example.map_maker.types.EntityTypes;
import com.example.map_maker.types.TileTypes;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MapLoader {

  /*
    [CONSTRUCTORS]
  */

  /**
   * @param _file the file where the map will be loaded from
   */
  public MapLoader(File _file){
    tileSets = new HashMap<String, Image[]>();
    this.file = _file;
  }

  /*
    [PUBLIC FIELDS]
  */



  /*
    [PUBLIC METHODS]
  */

  public Map load() throws IOException {

    JSONObject mapJson = new JSONObject(Files.readString(file.toPath()));

    int spriteSize = mapJson.getInt("spriteSize");
    int tileSize = mapJson.getInt("tileSize");
    int height = mapJson.getInt("height");
    int width = mapJson.getInt("width");
    Map map = new Map(width, height, tileSize);

    JSONArray tiles = mapJson.getJSONArray("tiles");
    for(int i = 0; i < tiles.length(); ++i){
      JSONObject tile = tiles.getJSONObject(i);
      int x = tile.getInt("x");
      int y = tile.getInt("y");
      TileTypes tileType = TileTypes.valueOf(tile.getString("tileType"));
      EntityTypes entityType = EntityTypes.valueOf(tile.getString("entityType"));

      int tileNumber = tile.getInt("tgi_tile_number");
      String fileName = tile.getString("tgi_file_name");

      Image image = getImageFromTileSet(fileName, tileNumber);
      if(image == null){
        loadTileSet(fileName, spriteSize);
        image = getImageFromTileSet(fileName, tileNumber);
      }

      TileGraphicInfo tgi = new TileGraphicInfo(tileNumber, fileName, image);

      map.tiles[y][x].tgi = tgi;
      map.tiles[y][x].tileType = tileType;
      map.tiles[y][x].entityType = entityType;

    }

    return map;
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private HashMap<String, Image[]> tileSets;
  private File file;

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  /**
   *
   * @param _fileName - only the name of tile set file
   * @param spriteSize - the width/height of a desired image inside the tile set
   */
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
