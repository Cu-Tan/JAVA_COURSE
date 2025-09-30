package com.example.map_maker;

import com.example.map_maker.map.Map;
import com.example.map_maker.types.EntityTypes;
import com.example.map_maker.types.TileTypes;

/**
 * Holds data for how to edit tiles inside a map grid and map
 */
public class MapPainter {

  /*
    [CONSTRUCTORS]
  */
  /*
    [PUBLIC FIELDS]
  */

  public Modes mode = Modes.NONE;

  public TileTypes tileType = TileTypes.NONE;
  public TileGraphicInfo tgi = null;

  public EntityTypes entityType = EntityTypes.NONE;

  public enum Modes{
    NONE, TILE, ENTITY
  }
  /*
    [PUBLIC METHODS]
  */

  public void paint(Map.Tile _mapTile){
    switch (mode){
      case TILE -> {
        if(tgi == null){
          return;
        }
        if(tileType == TileTypes.NONE){
          _mapTile.tgi = null;
          _mapTile.tileType = tileType;
        }
        else {
          _mapTile.tgi = tgi;
          _mapTile.tileType = tileType;
        }

      }
      case ENTITY -> {
        _mapTile.entityType = entityType;
      }
    }
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */
  /*
    [PRIVATE / PROTECTED METHODS]
  */
}
