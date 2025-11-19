package com.example.server.logic.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class UserDB {

  public UserDB(){
    if(!Files.exists(path)){
      try {
        Files.createFile(path);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public synchronized void saveUser(
    UserData _userData
  ) {
    try {

      String jsonContent = Files.readString(path);
      JSONArray usersJSON = null;

      if(jsonContent.isBlank()){
        usersJSON = new JSONArray();
      }
      else {
        usersJSON = new JSONArray(jsonContent);
      }

      JSONObject userJSON = new JSONObject();

      userJSON.put("id", _userData.ID);
      userJSON.put("username", _userData.username);

      usersJSON.put(userJSON);

      Files.writeString(path, usersJSON.toString(2));

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized ArrayList<UserData> loadUsers(){
    ArrayList<UserData> users = new ArrayList<>();

    try {

      String jsonContent = Files.readString(path);
      JSONArray usersJSON = null;

      if(jsonContent.isBlank()){
        return users;
      }
      else {
        usersJSON = new JSONArray(jsonContent);
      }

      for(int i = 0; i < usersJSON.length(); ++i){
        JSONObject userJSON = usersJSON.getJSONObject(i);
        users.add( new UserData(
            UUID.fromString(userJSON.getString("id")),
            userJSON.getString("username")
        ));
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return users;
  }

  private final Path path = Paths.get("users.json");

}
