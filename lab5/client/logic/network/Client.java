package com.example.client.logic.network;

import com.example.client.logic.Logger;
import com.example.client.logic.data.UserData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class Client {

  // region Singleton
  public static Client getInstance(){
    if(instance == null){
      instance = new Client();
    }
    return instance;
  }
  private static Client instance;
  private Client(){}
  // endregion

  public boolean connect(){
    try{
      clientSocket = new Socket("localhost", PORT);
      Thread clientListenThread = new Thread( new ClientListenRunnable(clientSocket) );
      clientListenThread.start();
      return true;
    }
    catch (Exception exception) {
      return false;
    }
  }
  public void disconnect(){
    try{
      if(clientSocket != null){
        clientSocket.close();
        clientSocket = null;
      }
    }
    catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  public void login(
    String _username
  ) {

    try {

      Logger.log("Sending command (1) login");

      OutputStream oStream = clientSocket.getOutputStream();
      oStream.write(1);
      SocketHelper.bufferString(oStream, _username, NetworkGlobals.BYTE_LENGTH);
      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void sendCreateGlobalRoom(
    String _roomName
  ) {
    try {

      Logger.log("Sending command (2) create global room");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(2);

      SocketHelper.bufferString(oStream, _roomName, NetworkGlobals.BYTE_LENGTH);

      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void sendFetchGlobalRooms(){
    try {

      Logger.log("Sending command (3) fetch global rooms");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(3);
      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendCreatePrivateRoom(
    String _username
  ) {
    try {

      Logger.log("Sending command (20) create private room");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(20);

      SocketHelper.bufferString(oStream, _username, NetworkGlobals.BYTE_LENGTH);

      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void sendFetchPrivateRooms(){

    try {

      Logger.log("Sending command (21) fetch global rooms");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(21);
      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void sendFetchRoomMessages(
    UUID _roomID
  ) {

    try {

      Logger.log("Sending command (4) fetch room messages");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(4);
      SocketHelper.bufferUUID(oStream, _roomID);

      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void sendMessage(
      UUID roomID,
      UUID userID,
      String message
  ) {

    try {

      Logger.log("Sending command (5) new message");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(5);

      SocketHelper.bufferUUID(oStream, roomID);
      SocketHelper.bufferUUID(oStream, userID);
      SocketHelper.bufferString(oStream, message, NetworkGlobals.BYTE_LENGTH);

      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void enterRoom(
    UUID _roomID
  ) {

    try{

      Logger.log("Sending command (10) enter room");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(10);
      SocketHelper.bufferUUID(oStream, _roomID);
      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
  public void exitRoom() {

    try{

      Logger.log("Sending command (11) exit room");

      OutputStream oStream = clientSocket.getOutputStream();

      oStream.write(11);
      oStream.flush();

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }


  public final UserData userData = new UserData(null, null);
  Socket clientSocket;
  private final int PORT = 6666;

}
