package com.example.map_maker.map;

import com.example.map_maker.EditorDefaults;
import com.example.map_maker.TileGraphicInfo;
import com.example.map_maker.types.EntityTypes;
import com.example.map_maker.types.TileTypes;
import javafx.scene.image.Image;

/**
 * Holds all map data (Tiles, width, height, tile size, spriteSize)
 */
public class Map {

  /*
    [CONSTRUCTORS]
  */

  public Map(int _width, int _height){
    this(_width, _height, EditorDefaults.DEFAULT_MAP_TILE_SIZE);
  }

  public Map(int _width, int _height, int _tileSize){
    this(_width, _height, _tileSize, EditorDefaults.DEFAULT_SPRITE_SIZE);
  }

  public Map(int _width, int _height, int _tileSize, int _spriteSize){

    this.width = _width;
    this.height = _height;
    this.tileSize = _tileSize;
    this.spriteSize = _spriteSize;

    initTiles();

  }

  /*
    [PUBLIC FIELDS]
  */

  public int width;
  public int height;
  public int tileSize;
  public int spriteSize;
  public Tile[][] tiles;

  public static class Tile {

    public TileTypes tileType = TileTypes.NONE;
    public EntityTypes entityType = EntityTypes.NONE;
    public TileGraphicInfo tgi;

    public Image getImage(){
      if(tgi == null){
        return null;
      }
      return this.tgi.getImage();
    }
    public TileTypes getType() { return this.tileType; }
    public TileGraphicInfo getTgi() { return this.tgi; }

  }

  /*
    [PUBLIC METHODS]
  */

  /**
   * Copies tiles from another map to the current one (truncates tiles if current map has smaller size)
   */
  public void copyTiles(Map _other){
    int rows = Math.min(this.height, _other.height);
    int cols = Math.min(this.width, _other.width);

    for(int row = 0; row < rows; ++row){
      for(int col = 0; col < cols; ++col){
        this.tiles[row][col] = _other.tiles[row][col];
      }
    }
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  /*
    [PRIVATE / PROTECTED METHODS]
  */
  private void initTiles(){
    tiles = new Tile[height][width];
    for(int row = 0; row < height; ++row){
      for(int col = 0; col < width; ++col){
        tiles[row][col] = new Tile();
      }
    }
  }




}
