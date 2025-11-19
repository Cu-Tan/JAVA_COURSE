package com.example.server.logic.data;

import java.util.UUID;

public class UserData {

  public static UserData withRandomID(
    String _username
  ) {
    return new UserData(
      UUID.randomUUID(),
      _username
    );
  }

  public UserData(
    UUID _ID,
    String _username
  ) {
    ID = _ID;
    username = _username;
  }

  public UUID ID;
  public String username;

}
