package com.example.server.logic.server;

import com.example.server.logic.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerRunnable implements Runnable {

  public ServerRunnable(
    ServerSocket _serverSocket
  ) {

    serverSocket = _serverSocket;

  }

  @Override
  public void run() {

    try{
      while(true){

        Socket clientSocket = serverSocket.accept();
        Logger.log("Client connected");
        Thread clientThread = new Thread( new ClientRunnable(clientSocket) );
        clientThread.start();

      }
    }
    catch (SocketException exception){
      // Needed to catch the exception when a server socket is closed and the thread can end
    }
    catch (Exception exception) {

      throw new RuntimeException(exception);

    }

  }

  private final ServerSocket serverSocket;

}