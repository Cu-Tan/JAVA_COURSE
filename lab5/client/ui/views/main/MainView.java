package com.example.client.ui.views.main;

import com.example.client.logic.data.MessageData;
import com.example.client.logic.data.RoomData;
import com.example.client.ui.chatroom.ChatRoomWindow;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MainView extends BorderPane {

  public MainView(){

    mainViewMenu = new MainViewMenu();
    this.setLeft(mainViewMenu);
    displayNoChatRoom();

  }

  public void displayChatRoom(
    ChatRoomWindow _chatRoom
  ){

    currentChatRoom = _chatRoom;
    this.setCenter(_chatRoom);

  }
  public void displayNoChatRoom(){

    currentChatRoom = null;
    this.setCenter( new Text("Select a chat room!") );

  }

  public void addNewGlobalRoom(
      RoomData _roomData
  ) {
    mainViewMenu.addNewGlobalRoom(_roomData);
  }
  public void updateGlobalRoomsList(
    ArrayList<RoomData> _roomDataList
  ) {
    mainViewMenu.updateGlobalRoomsList(_roomDataList);
  }

  public void addNewPrivateRoom(
    RoomData _roomData
  ) {
    mainViewMenu.addNewPrivateRoom(_roomData);
  }
  public void populatePrivateRooms(
    ArrayList<RoomData> _roomDataList
  ) {
    mainViewMenu.populatePrivateRooms(_roomDataList);
  }

  public void populateMessages(
      ArrayList<MessageData> _messages
  ) {
    if(currentChatRoom != null){
      currentChatRoom.populateMessages(_messages);
    }
  }

  public void addNewMessage(
      MessageData _messageData
  ) {
    if(currentChatRoom != null){
      currentChatRoom.addNewMessage(_messageData);
    }
  }

  private final MainViewMenu mainViewMenu;
  private ChatRoomWindow currentChatRoom = null;

}
