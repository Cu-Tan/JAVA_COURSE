package com.example.client.logic.data;

import java.util.UUID;

public class RoomData {

  public RoomData(
    UUID _ID,
    String _name
  ) {
    ID = _ID;
    name = _name;
  }

  public UUID ID;
  public String name;

}
