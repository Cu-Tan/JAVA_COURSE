package com.example.server.logic.server;

import com.example.server.logic.DisconnectException;
import com.example.server.logic.Logger;
import com.example.server.logic.SocketHelper;
import com.example.server.logic.User;
import com.example.server.logic.data.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class ClientRunnable implements Runnable {

  public ClientRunnable(
    Socket _clientSocket
  ) {
    clientSocket = _clientSocket;

    try{
      iStream = clientSocket.getInputStream();
      oStream = clientSocket.getOutputStream();
    }
    catch (Exception exception){
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void run() {

    Server.getInstance().addClient(this);

    boolean oStreamLocked = false;

    try{
      int commandNum;
      while( ( commandNum = iStream.read() ) != -1){

        oStreamLock.lock();
        oStreamLocked = true;

        handleCommand(commandNum);

        oStreamLock.unlock();
        oStreamLocked = false;

      }
      throw new DisconnectException();
    }
    catch (SocketException exception){
      // TODO: do nothing but log socket closing if needed
      handleDisconnect();
    }
    catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    catch (DisconnectException e) {
      handleDisconnect();
    }
    finally {
      if(oStreamLocked){
        oStreamLock.unlock();
      }
    }
  }

  public void stop() throws IOException {

    clientSocket.close();

  }

  public void sendMessage(
    MessageDataNetwork _messageData
  ) throws IOException {

      oStream.write(100);
      SocketHelper.bufferString(oStream, _messageData.username, NetworkGlobals.BYTE_LENGTH);
      SocketHelper.bufferString(oStream, _messageData.message, NetworkGlobals.BYTE_LENGTH);
      oStream.flush();

  }

  public void sendNewRoom(
      RoomData _roomData
  ) throws IOException {

    oStream.write(99);
    SocketHelper.bufferUUID(oStream, _roomData.ID);
    SocketHelper.bufferString(oStream, _roomData.name, NetworkGlobals.BYTE_LENGTH);
    oStream.flush();

  }

  public void sendNewPrivateRoom(
    PrivateRoomData _roomData
  ) throws IOException {

    oStream.write(20);
    SocketHelper.bufferUUID(oStream, _roomData.ID);

    if(user.userData.ID.equals(_roomData.user1.ID)){
      SocketHelper.bufferString(oStream, _roomData.user2.username, NetworkGlobals.BYTE_LENGTH);
    }
    else {
      SocketHelper.bufferString(oStream, _roomData.user1.username, NetworkGlobals.BYTE_LENGTH);
    }

    oStream.flush();

  }

  public User getUser(){
    return user;
  }

  public ReentrantLock oStreamLock = new ReentrantLock();
  private User user = null;
  private final InputStream iStream;
  private final OutputStream oStream;
  private final Socket clientSocket;

  private void handleCommand(int _commandNum) throws IOException, DisconnectException {

    switch (_commandNum){
      case 1:
        handleLogin();
        break;
      case 2:
        handleCreateGlobalRoom();
        break;
      case 3:
        handleFetchGlobalRooms();
        break;
      case 4:
        handleFetchRoomMessages();
        break;
      case 5:
        handleNewMessage();
        break;
      case 10:
        handleEnterRoom();
        break;
      case 11:
        handleLeaveRoom();
        break;
      case 20:
        handleCreatePrivateRoom();
        break;
      case 21:
        fetchPrivateRooms();
        break;
    }

  }

  private void handleLogin() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (1) login ");

    String username = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    DataRepository dataRepository = DataRepository.getInstance();

    // check if the user is currently logged in (if so send ERR code)
    if(dataRepository.userLoggedIn(username)){
      Logger.log("User " + getClientName() + " is already logged in!");
      sendErr();
      return;
    }

    // check if the user already exits
    UserData userData = dataRepository.findUserByUsername(username);
    if(userData == null){
      // create a new user and save it to DB
      userData = UserData.withRandomID(username);
      dataRepository.addUserToDB(userData);
    }

    User newUser = new User(userData);
    dataRepository.addUser(newUser);
    this.user = newUser;

    sendOKLogin();
  }
  private void handleDisconnect(){
    Logger.log("Client "+ getClientName() + " closed the connection");
    if(user != null){
      DataRepository.getInstance().removeUser(user);
    }
    Server.getInstance().removeClient(this);
  }

  private void handleCreateGlobalRoom() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (2) create global room ");

    String roomName = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    RoomData roomData = RoomData.withRandomID(roomName);
    DataRepository.getInstance().addRoomToDB(roomData);

    // Send ok room created
    oStream.write(200);
    oStream.write(2);
    oStream.flush();

    // Send new room to all connected clients
    Server.getInstance().sendNewRoom(roomData);

  }
  private void handleFetchGlobalRooms() throws IOException {

    Logger.log("Received command from client " + getClientName() + " (3) fetch global rooms ");

    ArrayList<RoomData> roomDataList = DataRepository.getInstance().fetchRoomsFromDB();

    oStream.write(200);
    oStream.write(3);

    SocketHelper.bufferLength(oStream, roomDataList.size(), NetworkGlobals.BYTE_LENGTH);

    for(RoomData roomData: roomDataList){
      bufferRoomData(roomData);
    }

    oStream.flush();

  }

  private void handleFetchRoomMessages() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (4) fetch room messages ");

    UUID roomID = SocketHelper.readUUID(iStream);
    ArrayList<MessageDataNetwork> messages = DataRepository.getInstance().fetchRoomMessagesByRoomID(roomID);

    oStream.write(200);
    oStream.write(4);

    SocketHelper.bufferLength(oStream, messages.size(), NetworkGlobals.BYTE_LENGTH);

    for(MessageDataNetwork messageData: messages){
      bufferMessage(messageData);
    }

    oStream.flush();

  }
  private void handleNewMessage() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (5) new message ");

    UUID roomID = SocketHelper.readUUID(iStream);
    UUID userID = SocketHelper.readUUID(iStream);
    String message = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    MessageData messageData = new MessageData(
      0,
        roomID,
        userID,
        message
    );

    DataRepository.getInstance().addMessageToDB(messageData);

    Server.getInstance().sendNewMessage(messageData);

  }

  private void handleEnterRoom() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (10) enter room ");

    user.currentRoomID = SocketHelper.readUUID(iStream);

  }
  private void handleLeaveRoom() {

    Logger.log("Received command from client " + getClientName() + " (11) leave room ");

    user.currentRoomID = null;

  }

  private void handleCreatePrivateRoom() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (20) create new private room ");

    String otherUserUsername = SocketHelper.readString(iStream, NetworkGlobals.BYTE_LENGTH);

    // Check if the requested username is the same as client username. If so do nothing
    if(user.userData.username.equals(otherUserUsername)){
      return;
    }

    // Check if the requested user actually exists in the DB
    DataRepository dataRepository = DataRepository.getInstance();
    UserData userData = dataRepository.findUserByUsername(otherUserUsername);
    if(userData == null){
      return;
    }

    // Check if the private room exists
    if(dataRepository.checkPrivateRoomExists(user.userData.ID, userData.ID)){
      return;
    }

    PrivateRoomData roomData = PrivateRoomData.withRandomID(
        user.userData,
        userData
    );
    DataRepository.getInstance().addPrivateRoomToDB(roomData);

    // Notify all users that need to know that a private room has been created

    Server.getInstance().notifyOfPrivateRoomCreation(roomData);

  }
  private void fetchPrivateRooms() throws IOException, DisconnectException {

    Logger.log("Received command from client " + getClientName() + " (21) fetch private rooms ");

    // If no user is assigned to the client then do nothing
    if(user == null){
      return;
    }

    ArrayList<PrivateRoomData> privateRoomDataList = DataRepository.getInstance().fetchPrivateRoomsForUser(user.userData.ID);

    oStream.write(200);
    oStream.write(21);

    SocketHelper.bufferLength(oStream, privateRoomDataList.size(), NetworkGlobals.BYTE_LENGTH);

    for(PrivateRoomData roomData: privateRoomDataList){

      SocketHelper.bufferUUID(oStream, roomData.ID);

      if(user.userData.ID.equals( roomData.user1.ID )){
        SocketHelper.bufferString(oStream, roomData.user2.username, NetworkGlobals.BYTE_LENGTH);
      }
      else {
        SocketHelper.bufferString(oStream, roomData.user1.username, NetworkGlobals.BYTE_LENGTH);
      }

    }

  }

  private void bufferRoomData (
    RoomData _roomData
  ) throws IOException {

    SocketHelper.bufferUUID(oStream, _roomData.ID);
    SocketHelper.bufferString(oStream, _roomData.name, NetworkGlobals.BYTE_LENGTH);

  }
  private void bufferMessage(
    MessageDataNetwork _messageData
  ) throws IOException {

    SocketHelper.bufferString(oStream, _messageData.username, NetworkGlobals.BYTE_LENGTH);
    SocketHelper.bufferString(oStream, _messageData.message, NetworkGlobals.BYTE_LENGTH);

  }

  private void sendErr() throws IOException {

    Logger.log("Sending 255 ERR");
    OutputStream outputStream = clientSocket.getOutputStream();
    outputStream.write(255);
    outputStream.flush();

  }
  private void sendOKLogin() throws IOException {

    Logger.log("Sending 200 OK (1 - login)");
    OutputStream outputStream = clientSocket.getOutputStream();
    outputStream.write(200);
    outputStream.write(1);

    SocketHelper.bufferUUID(oStream, this.user.userData.ID);
    SocketHelper.bufferString(oStream, this.user.userData.username, NetworkGlobals.BYTE_LENGTH);

    outputStream.flush();

  }
  private String getClientName(){

    return new String("(" + ((user == null) ? "UNKNOWN" : user.userData.username )+ ")");

  }

}
