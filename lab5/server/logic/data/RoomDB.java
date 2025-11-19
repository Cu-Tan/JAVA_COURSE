package com.example.server.logic.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class RoomDB {

  public RoomDB(){
    if(!Files.exists(path)){
      try {
        Files.createFile(path);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public synchronized void saveRoom(
      RoomData _roomData
  ) {
    try {

      String jsonContent = Files.readString(path);
      JSONArray roomsJSON = null;

      if(jsonContent.isBlank()){
        roomsJSON = new JSONArray();
      }
      else {
        roomsJSON = new JSONArray(jsonContent);
      }

      JSONObject roomJSON = new JSONObject();

      roomJSON.put("id", _roomData.ID);
      roomJSON.put("name", _roomData.name);

      roomsJSON.put(roomJSON);

      Files.writeString(path, roomsJSON.toString(2));

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized ArrayList<RoomData> loadRooms(){
    ArrayList<RoomData> rooms = new ArrayList<>();

    try {

      String jsonContent = Files.readString(path);
      JSONArray roomsJSON = null;

      if(jsonContent.isBlank()){
        return rooms;
      }
      else {
        roomsJSON = new JSONArray(jsonContent);
      }

      for(int i = 0; i < roomsJSON.length(); ++i){
        JSONObject roomJSON = roomsJSON.getJSONObject(i);
        rooms.add( new RoomData(
            UUID.fromString(roomJSON.getString("id")),
            roomJSON.getString("name")
        ));
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return rooms;
  }


  private final Path path = Paths.get("rooms.json");

}