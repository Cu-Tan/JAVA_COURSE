package com.example.server.logic.data;

import java.util.UUID;

public class RoomData {

  public static RoomData withRandomID(
      String _name
  ) {
    return new RoomData(
      UUID.randomUUID(),
      _name
    );
  }

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
