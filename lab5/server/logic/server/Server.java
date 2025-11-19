package com.example.server.logic.server;

import com.example.server.logic.User;
import com.example.server.logic.data.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Handles client and server connection and data transfer
 */
public class Server {

  // region [Singleton]
  public static Server getInstance(){
    return instance;
  }
  private static final Server instance = new Server();
  private Server(){}
  // endregion

  public void start() throws Exception {

    serverSocket = new ServerSocket(PORT);

    Thread serverThread = new Thread( new ServerRunnable(serverSocket) );
    serverThread.start();

  }

  public void stop() {

    try{
      if(serverSocket != null){
        serverSocket.close();
      }
      for(ClientRunnable client: clients){
        client.stop();
      }
    }
    catch (Exception exception){
      throw new RuntimeException(exception);
    }

  }

  public int getPort(){
    return serverSocket.getLocalPort();
  }

  public synchronized void addClient(ClientRunnable _client){
    clients.add(_client);
  }
  public synchronized void removeClient(ClientRunnable _client){
    clients.remove(_client);
  }

  public void sendNewMessage(
    MessageData _messageData
  ) {

    ArrayList<ClientRunnable> clientsToNotify = new ArrayList<>();

    // Find clients currently connected to the room the message was sent in
    for(ClientRunnable clientRunnable: clients){
      User user = clientRunnable.getUser();
      if(user != null && user.currentRoomID != null && user.currentRoomID.equals(_messageData.roomID)){
        clientsToNotify.add(clientRunnable);
      }
    }

    // Find the username of the user who sent the message
    UserData userData = DataRepository.getInstance().getUserByID(_messageData.userID);

    // Format message for sending on the network to the client
    MessageDataNetwork messageDataNetwork = new MessageDataNetwork(
        userData.username,
        _messageData.message
    );

    // Send message to all clients
    for(ClientRunnable client: clientsToNotify){

      client.oStreamLock.lock();

      try {
        client.sendMessage(messageDataNetwork);
      }
      catch (IOException e) {
        // No special case for IO exception for now
      } finally {
        client.oStreamLock.unlock();
      }


    }

  }

  public void sendNewRoom(
    RoomData _roomData
  ) {

    for(ClientRunnable client: clients){
      // Check if user is logged in
      if(client.getUser() != null){

        client.oStreamLock.lock();

        try {
          client.sendNewRoom(_roomData);
        }
        catch (IOException e) {
          // No special case for IO exception for now
        } finally {
          client.oStreamLock.unlock();
        }

      }
    }

  }

  public void notifyOfPrivateRoomCreation(
    PrivateRoomData _roomData
  ) {

    ArrayList<ClientRunnable> clientsToNotify = new ArrayList<>();

    for(ClientRunnable client: clients){

      User user = client.getUser();
      if(user == null){
        continue;
      }

      if( user.userData.ID.equals(_roomData.user1.ID) ||  user.userData.ID.equals(_roomData.user2.ID) ) {
        clientsToNotify.add(client);
      }

    }

    for(ClientRunnable client: clientsToNotify){

      client.oStreamLock.lock();

      try {
        client.sendNewPrivateRoom(_roomData);
      }
      catch (IOException e) {
        // No special case for IO exception for now
      } finally {
        client.oStreamLock.unlock();
      }

    }

  }

  private final int PORT = 6666;

  private ServerSocket serverSocket;

  private ServerRunnable server;
  private final ArrayList<ClientRunnable> clients = new ArrayList<>();

}
