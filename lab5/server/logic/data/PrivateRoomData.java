package com.example.server.logic.data;

import java.util.UUID;

public class PrivateRoomData {

  public static PrivateRoomData withRandomID(
    UserData _user1,
    UserData _user2
  ) {
    return new PrivateRoomData(
        UUID.randomUUID(),
        _user1,
        _user2
    );
  }

  public PrivateRoomData(
    UUID _ID,
    UserData _user1,
    UserData _user2
  ) {
    ID = _ID;
    user1 = _user1;
    user2 = _user2;
  }

  public UUID ID;
  public UserData user1;
  public UserData user2;

}
