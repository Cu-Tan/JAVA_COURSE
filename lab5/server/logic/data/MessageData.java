package com.example.server.logic.data;

import java.util.UUID;

public class MessageData {

  public MessageData(
    int _sequenceNum,
    UUID _roomID,
    UUID _userID,
    String _message
  ) {
    sequenceNum = _sequenceNum;
    roomID = _roomID;
    userID = _userID;
    message = _message;
  }

  public int sequenceNum;
  public UUID roomID;
  public UUID userID;
  public String message;

}
