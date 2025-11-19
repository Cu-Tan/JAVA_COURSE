package com.example.client.ui.views.main;

import com.example.client.logic.network.Client;
import com.example.client.logic.data.RoomData;
import com.example.client.ui.ViewManager;
import com.example.client.ui.chatroom.ChatRoomWindow;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MainViewMenu extends VBox {

  public MainViewMenu(){

    globalRoomsBox = new GlobalRooms();
    privateRoomsBox = new PrivateRooms();

    this.getChildren().addAll(globalRoomsBox, privateRoomsBox);

  }

  public void addNewGlobalRoom(
      RoomData _roomData
  ) {
    globalRoomsBox.addNewRoom(_roomData);
  }
  public void updateGlobalRoomsList(
    ArrayList<RoomData> _roomDataList
  ) {
    globalRoomsBox.updateRoomsList(_roomDataList);
  }

  public void addNewPrivateRoom(
      RoomData _roomData
  ) {
    privateRoomsBox.addNewRoom(_roomData);
  }
  public void populatePrivateRooms(
      ArrayList<RoomData> _roomDataList
  ) {
    privateRoomsBox.populateRooms(_roomDataList);
  }

  private static class GlobalRooms extends VBox {

    public GlobalRooms(){

      Text headerText = new Text("Global rooms");

      Label roomNameLabel = new Label("Room name:");
      TextField roomNameField = new TextField();
      Button createRoomButton = new Button("Create room");

      createRoomButton.setOnAction( e -> {

        Client.getInstance().sendCreateGlobalRoom(roomNameField.getText());

      });

      HBox roomCreationLayout = new HBox(roomNameLabel, roomNameField, createRoomButton);
      roomCreationLayout.setAlignment(Pos.CENTER);

      // Limit the rooms list and make it scrollable
      ScrollPane roomsContainer = new ScrollPane();
      roomsContainer.setContent(rooms);
      roomsContainer.maxHeight(200);

      this.setAlignment(Pos.CENTER);
      this.getChildren().addAll(
          headerText,
          roomCreationLayout,
          roomsContainer
      );

      Client.getInstance().sendFetchGlobalRooms();

    }


    public void addNewRoom(
        RoomData _roomData
    ) {

      Button newRoomButton = new Button(_roomData.name);

      newRoomButton.setOnAction( e -> {

        ChatRoomWindow chatRoomWindow = new ChatRoomWindow(_roomData);
        ViewManager.getInstance().displayChatRoom(chatRoomWindow);

      });

      rooms.getChildren().add(newRoomButton);
    }
    public void updateRoomsList(
      ArrayList<RoomData> _roomDataList
    ) {

      rooms.getChildren().clear();

      for(RoomData roomData: _roomDataList){
        addNewRoom(roomData);
      }

    }

    private final VBox rooms = new VBox();

  }
  private static class PrivateRooms extends VBox {

    public PrivateRooms(){

      Text headerText = new Text("Private rooms");

      Label usernameLabel = new Label("Username:");
      TextField usernameField = new TextField();
      Button createRoomButton = new Button("Create room");

      createRoomButton.setOnAction( e -> {

        Client.getInstance().sendCreatePrivateRoom(usernameField.getText());

      });

      HBox roomCreationLayout = new HBox(usernameLabel, usernameField, createRoomButton);
      roomCreationLayout.setAlignment(Pos.CENTER);

      // Limit the rooms list and make it scrollable
      ScrollPane roomsContainer = new ScrollPane();
      roomsContainer.setContent(rooms);
      roomsContainer.maxHeight(200);

      this.setAlignment(Pos.CENTER);
      this.getChildren().addAll(
        headerText,
        roomCreationLayout,
        roomsContainer
      );

      Client.getInstance().sendFetchPrivateRooms();

    }

    public void addNewRoom(
        RoomData _roomData
    ) {

      Button newRoomButton = new Button(_roomData.name);

      newRoomButton.setOnAction( e -> {

        ChatRoomWindow chatRoomWindow = new ChatRoomWindow(_roomData);
        ViewManager.getInstance().displayChatRoom(chatRoomWindow);

      });

      rooms.getChildren().add(newRoomButton);

    }
    public void populateRooms(
        ArrayList<RoomData> _roomDataList
    ) {

      rooms.getChildren().clear();

      for(RoomData roomData: _roomDataList){
        addNewRoom(roomData);
      }

    }

    private final VBox rooms = new VBox();

  }

  private final GlobalRooms globalRoomsBox;
  private final PrivateRooms privateRoomsBox;

}
