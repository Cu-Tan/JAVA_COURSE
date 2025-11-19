package com.example.client.logic.network;

import com.example.client.logic.Logger;
import com.example.client.logic.data.MessageData;
import com.example.client.logic.data.RoomData;
import com.example.client.ui.ViewManager;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Handles server notifications sent to the client
 */
public class ClientListenRunnable implements Runnable {

  public ClientListenRunnable(
    Socket _clientSocket
  ) {
    clientSocket = _clientSocket;
  }

  @Override
  public void run() {
    try{
      iStream = clientSocket.getInputStream();
      int commandNum;
      while( ( commandNum = iStream.read() ) != -1){

        handleCommand(commandNum);

      }

      // Temporary exception because there is no graceful exit yet
      throw new DisconnectException();
    }
    catch (SocketException exception){
      // TODO: HANDLE LOCAL SOCKET CLOSED
    }
    catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    catch (DisconnectException e) {
      Logger.log("The server has closed the connection");
      Platform.runLater( () -> {
        Client.getInstance().userData.ID = null;
        Client.getInstance().userData.username = null;
        ViewManager.getInstance().displayConnectView();
      });
    }
  }

  private InputStream iStream;
  private final Socket clientSocket;

  private void handleCommand(int _commandNum) throws IOException, DisconnectException {

    switch (_commandNum){
      case 20:
        handleNewPrivateRoom();
        break;
      case 99:
        handleNewRoom();
        break;
      case 100:
        handleNewMessage();
        break;
      case 200:
        handleOKCommand();
        break;
      case 255:
        break;
    }

  }

  private void handleNewRoom() throws IOException, DisconnectException {

    Logger.log("Received command from server (99) new global room created");

    RoomData roomData = readRoomData();
    Platform.runLater( () -> ViewManager.getInstance().addNewGlobalRoom(roomData) );

  }
  private void handleNewPrivateRoom() throws IOException, DisconnectException {

    Logger.log("Received command from server (20) new global room created");

    RoomData roomData = readRoomData();
    Platform.runLater( () -> ViewManager.getInstance().addNewPrivateRoom(roomData) );

  }

  private void handleNewMessage() throws IOException, DisconnectException {

    String username = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);
    String message = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    MessageData messageData = new MessageData(
        username,
        message
    );

    Platform.runLater( () -> ViewManager.getInstance().addNewMessage(messageData) );

  }

  private void handleOKCommand() throws IOException, DisconnectException {
    int commandNum = iStream.read();

    if(commandNum == -1){
      throw new DisconnectException();
    }

    switch (commandNum){
      case 1:

        handleOKLogin();
        break;

      case 2:

        handleOKCreateGlobalRoom();
        break;

      case 3:

        handleOKFetchGlobalRooms();
        break;

      case 4:

        handleOKFetchRoomMessages();
        break;

      case 21:

        handleOkFetchPrivateRooms();
        break;

    }
  }

  private void handleOKLogin() throws IOException, DisconnectException {

    Logger.log("Received command from server (200 - 1) ok login");

    UUID userID = SocketHelper.readUUID(iStream);
    String username = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    Client.getInstance().userData.ID = userID;
    Client.getInstance().userData.username = username;

    Platform.runLater( () -> ViewManager.getInstance().displayMainView());

  }
  private void handleOKCreateGlobalRoom() throws IOException, DisconnectException {

    Logger.log("Received command from server (200 - 2) ok create global room");

  }
  private void handleOKFetchGlobalRooms() throws IOException, DisconnectException {

    Logger.log("Received command from server (200 - 3) ok fetch global rooms");

    int roomCount = SocketHelper.readLength(iStream, NetworkGlobals.BYTE_LENGTH);

    ArrayList<RoomData> roomDataList = new ArrayList<>();
    for(int i = 0; i < roomCount; ++i){
      roomDataList.add( readRoomData() );
    }

    Platform.runLater( () -> ViewManager.getInstance().updateGlobalRoomsList(roomDataList));
  }
  private void handleOkFetchPrivateRooms() throws IOException, DisconnectException {

    Logger.log("Received command from server (200 - 21) ok fetch private rooms");

    int roomCount = SocketHelper.readLength(iStream, NetworkGlobals.BYTE_LENGTH);

    ArrayList<RoomData> roomDataList = new ArrayList<>();
    for(int i = 0; i < roomCount; ++i){
      roomDataList.add( readRoomData() );
    }

    Platform.runLater( () -> ViewManager.getInstance().populatePrivateRooms(roomDataList));

  }
  private void handleOKFetchRoomMessages() throws IOException, DisconnectException {

    Logger.log("Received command from server (200 - 4) ok fetch room messages");

    int messageCount = SocketHelper.readLength(iStream, NetworkGlobals.BYTE_LENGTH);

    ArrayList<MessageData> messageDataList = new ArrayList<>();

    for(int i = 0; i < messageCount; ++i){

      String username = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);
      String message = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

      messageDataList.add( new MessageData(
          username,
          message
      ));

    }

    Platform.runLater( () -> ViewManager.getInstance().populateMessages(messageDataList));

  }


  private RoomData readRoomData() throws IOException, DisconnectException {

    UUID roomID = SocketHelper.readUUID(iStream);
    String roomName = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    return new RoomData(
      roomID,
      roomName
    );
  }

}
