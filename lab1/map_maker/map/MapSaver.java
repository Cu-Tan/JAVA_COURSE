package com.example.map_maker.map;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapSaver {

  /*
    [CONSTRUCTORS]
  */

  /**
   *
   * @param _file the file where the map will be written to
   * @param _map a map to write to a file
   */
  public MapSaver(File _file, Map _map){
    this.file = _file;
    this.map = _map;
  }

  /*
    [PUBLIC FIELDS]
  */
  /*
    [PUBLIC METHODS]
  */

  public void save() throws IOException {
    JSONObject mapJson = new JSONObject();

    mapJson.put("spriteSize", map.spriteSize);
    mapJson.put("tileSize", map.tileSize);
    mapJson.put("height", map.height);
    mapJson.put("width", map.width);

    JSONArray tiles = new JSONArray();

    for(int row = 0; row < map.height; ++row){
      for(int col = 0; col < map.width; ++col){

        if(map.tiles[row][col].getTgi() == null){
          continue;
        }

        JSONObject tile = new JSONObject();

        Map.Tile currTile = map.tiles[row][col];

        tile.put("x", col);
        tile.put("y", row);
        tile.put("tileType", currTile.getType().toString());
        tile.put("entityType", currTile.entityType.toString());
        tile.put("tgi_tile_number", currTile.getTgi().getTileNumber());
        tile.put("tgi_file_name", currTile.getTgi().getFileName());

        tiles.put(tile);
      }
    }

    mapJson.put("tiles", tiles);

    FileWriter fw = new FileWriter(file);
    fw.write(mapJson.toString(2));
    fw.flush();
    fw.close();
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private final File file;
  private final Map map;

  /*
    [PRIVATE / PROTECTED METHODS]
  */
}
