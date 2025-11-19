package com.example.client.ui.views;

import com.example.client.logic.network.Client;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ConnectionView extends VBox {

  public ConnectionView(
    Runnable _onConnect
  ) {

    Text connectionText = new Text("Not connected to the server!");

    Button connectButton = new Button("Connect");
    connectButton.setOnAction( e -> {
      if(Client.getInstance().connect()){
        _onConnect.run();
      }
    });

    this.getChildren().addAll(connectionText, connectButton);
    this.setAlignment(Pos.CENTER);

  }


}
