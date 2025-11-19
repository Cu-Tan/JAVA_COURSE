package com.example.client.ui;

import com.example.client.logic.data.MessageData;
import com.example.client.logic.data.RoomData;
import com.example.client.ui.chatroom.ChatRoomWindow;
import com.example.client.ui.views.ConnectionView;
import com.example.client.ui.views.LoginView;
import com.example.client.ui.views.main.MainView;
import javafx.scene.Scene;

import java.util.ArrayList;

public class ViewManager {

  // region Singleton

  public static ViewManager getInstance(){
    if(instance == null){
      instance = new ViewManager();
    }
    return instance;
  }
  private ViewManager(){}
  private static ViewManager instance;

  // endregion

  public void init(
    Scene _scene
  ) {
    scene = _scene;
  }

  public void displayConnectView(){

    ConnectionView connectionView = new ConnectionView(this::displayLoginView);
    connectionView.prefWidthProperty().bind(scene.widthProperty());
    connectionView.prefHeightProperty().bind(scene.heightProperty());

    scene.setRoot(connectionView);

  }
  public void displayLoginView(){

    LoginView loginView = new LoginView();
    loginView.prefWidthProperty().bind(scene.widthProperty());
    loginView.prefHeightProperty().bind(scene.heightProperty());

    scene.setRoot(loginView);

  }
  public void displayMainView(){

    mainView = new MainView();
    mainView.prefWidthProperty().bind(scene.widthProperty());
    mainView.prefHeightProperty().bind(scene.heightProperty());

    scene.setRoot(mainView);

  }

  public void displayChatRoom(
    ChatRoomWindow _chatRoom
  ) {
    if(mainView == null){
      return;
    }
    mainView.displayChatRoom(_chatRoom);
  }
  public void displayNoChatRoom(){
    if(mainView == null){
      return;
    }
    mainView.displayNoChatRoom();
  }

  public void addNewGlobalRoom(
    RoomData _roomData
  ) {
    mainView.addNewGlobalRoom(_roomData);
  }

  public void updateGlobalRoomsList(
      ArrayList<RoomData> _roomDataList
  ) {
    if(mainView != null){
      mainView.updateGlobalRoomsList(_roomDataList);
    }
  }

  public void addNewPrivateRoom(
      RoomData _roomData
  ) {

    if(mainView == null) {
      return;
    }

    mainView.addNewPrivateRoom(_roomData);

  }
  public void populatePrivateRooms(
      ArrayList<RoomData> _roomDataList
  ) {

    if(mainView == null) {
      return;
    }

    mainView.populatePrivateRooms(_roomDataList);

  }

  public void populateMessages(
      ArrayList<MessageData> _messages
  ) {
    if(mainView != null){
      mainView.populateMessages(_messages);
    }
  }

  public void addNewMessage(
    MessageData _messageData
  ) {
    if(mainView != null){
      mainView.addNewMessage(_messageData);
    }
  }


  MainView mainView = null;
  private Scene scene;

}
