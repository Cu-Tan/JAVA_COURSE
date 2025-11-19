package com.example.server.logic.data;

import javafx.scene.chart.PieChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class PrivateRoomDB {

  public PrivateRoomDB(){
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
      PrivateRoomData _roomData
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
      roomJSON.put("user1_id", _roomData.user1.ID);
      roomJSON.put("user2_id", _roomData.user2.ID);

      roomsJSON.put(roomJSON);

      Files.writeString(path, roomsJSON.toString(2));

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized ArrayList<PrivateRoomData> loadRooms(){

    ArrayList<PrivateRoomData> rooms = new ArrayList<>();

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

        UUID user1ID = UUID.fromString(roomJSON.getString("user1_id"));
        UUID user2ID = UUID.fromString(roomJSON.getString("user2_id"));

        UserData user1 = DataRepository.getInstance().getUserByID(user1ID);
        UserData user2 = DataRepository.getInstance().getUserByID(user2ID);

        rooms.add( new PrivateRoomData(

            UUID.fromString(roomJSON.getString("id")),
            user1,
            user2

        ));
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return rooms;
  }


  private final Path path = Paths.get("private_rooms.json");

}
