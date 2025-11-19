package com.example.server.logic;

import com.example.server.logic.data.UserData;

import java.util.UUID;

public class User {

  public User(
    UserData _userData
  ) {
    userData = _userData;
    currentRoomID = null;
  }

  public void setCurrentRoomID(
    UUID _roomID
  ) {
    currentRoomID = _roomID;
  }

  public UserData userData;
  public UUID currentRoomID;

}
