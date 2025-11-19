package com.example.server.logic.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class MessageDB {

  public MessageDB(){
    if(!Files.exists(path)){
      try {
        Files.createFile(path);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public synchronized void saveMessage(
    MessageData _messageData
  ){

    try {

      String jsonContent = Files.readString(path);
      JSONArray messagesJSON = null;

      if(jsonContent.isBlank()){
        messagesJSON = new JSONArray();
      }
      else {
        messagesJSON = new JSONArray(jsonContent);
      }

      JSONObject messageJSON = new JSONObject();

      messageJSON.put("seq_id", _messageData.sequenceNum);
      messageJSON.put("room_id", _messageData.roomID);
      messageJSON.put("user_id", _messageData.userID);
      messageJSON.put("message", _messageData.message);

      messagesJSON.put(messageJSON);

      Files.writeString(path, messagesJSON.toString(2));

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public synchronized ArrayList<MessageData> loadMessages(){
    ArrayList<MessageData> messages = new ArrayList<>();

    try {

      String jsonContent = Files.readString(path);
      JSONArray messagesJSON = null;

      if(jsonContent.isBlank()){
        return messages;
      }
      else {
        messagesJSON = new JSONArray(jsonContent);
      }

      for(int i = 0; i < messagesJSON.length(); ++i){
        JSONObject messageJSON = messagesJSON.getJSONObject(i);
        messages.add( new MessageData(
          messageJSON.getInt("seq_id"),
          UUID.fromString(messageJSON.getString("room_id")),
          UUID.fromString(messageJSON.getString("user_id")),
          messageJSON.getString("message")
        ));
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return messages;
  }

  private final Path path = Paths.get("messages.json");

}
