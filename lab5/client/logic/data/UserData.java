package com.example.client.logic.data;

import java.util.UUID;

public class UserData {

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
