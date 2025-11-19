package com.example.client.ui.chatroom;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ChatRoomWindowHeader extends BorderPane {

  public ChatRoomWindowHeader(
      String _chatName,
      Runnable _onExit
  ){

    Button exitRoom = new Button("Exit");
    exitRoom.setOnAction( e -> _onExit.run() );

    Text chatNameText = new Text(_chatName);

    this.setLeft(exitRoom);
    this.setCenter(chatNameText);

  }

}
