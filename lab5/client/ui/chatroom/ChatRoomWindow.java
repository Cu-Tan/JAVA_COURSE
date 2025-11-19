package com.example.client.ui.chatroom;

import com.example.client.logic.network.Client;
import com.example.client.logic.data.MessageData;
import com.example.client.logic.data.RoomData;
import com.example.client.logic.data.UserData;
import com.example.client.ui.ViewManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ChatRoomWindow extends BorderPane {

  public ChatRoomWindow(
    RoomData _roomData
  ) {

    roomData = _roomData;

    messagesPane.setFitToWidth(true);
    this.setCenter(messagesPane);


    this.setTop( new ChatRoomWindowHeader(
        _roomData.name,
        () -> {

          // Tell the server that this room was exited by current client
          Client client = Client.getInstance();
          client.exitRoom();

          ViewManager.getInstance().displayNoChatRoom();
        }
    ));

    TextArea newMessageField = new TextArea();
    Button sendMessageButton = new Button("Send");
    HBox newMessageLayout = new HBox(newMessageField, sendMessageButton);
    this.setBottom(newMessageLayout);

    sendMessageButton.setOnAction( e -> {
      if(!newMessageField.getText().isBlank()){
        Client client = Client.getInstance();
        client.sendMessage(
          _roomData.ID,
          client.userData.ID,
          newMessageField.getText()
        );
      }
    });

    sendMessageButton.setMinWidth(Region.USE_PREF_SIZE);

    newMessageField.setPrefRowCount(5);
    newMessageField.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(newMessageField, Priority.ALWAYS);
    newMessageLayout.setMaxWidth(Double.MAX_VALUE);

    // Send info to the server that this room was joined
    Client client = Client.getInstance();
    client.enterRoom(_roomData.ID);

    // Request all room messages from server
    client.sendFetchRoomMessages(_roomData.ID);

  }

  public void populateMessages(
    ArrayList<MessageData> _messages
  ) {
    messages = _messages;
    displayMessages();
  }

  public void addNewMessage(
      MessageData _messageData
  ) {
    messages.add(_messageData);
    // TODO: optimize to ADD the new message instead of redrawing all messages
    displayMessages();
  }

  private final ScrollPane messagesPane = new ScrollPane();

  private final RoomData roomData;
  private ArrayList<MessageData> messages;

  private void displayMessages(){

    UserData userData = Client.getInstance().userData;

    VBox messagesBox = new VBox();
    messagesBox.setPadding( new Insets(8) );
    messagesBox.setSpacing(8);

    for(MessageData messageData: messages){

      Text fullMessage = new Text("[" + messageData.username + "]\n" + messageData.message);
      fullMessage.setWrappingWidth(400);

      HBox textBox = new HBox(fullMessage);
      textBox.setPadding( new Insets(8) );

      HBox textLayoutBox = new HBox(textBox);
      textLayoutBox.setMaxWidth(Double.MAX_VALUE);

      // Different display if the message belongs to you

      if(userData.username.equals(messageData.username)){
        textLayoutBox.setAlignment(Pos.CENTER_RIGHT);
        textBox.setBackground( Background.fill(Color.color((double) 144 / 255, (double) 213 / 255, (double) 255 / 255)) );
      } else {
        textLayoutBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setBackground( Background.fill(Color.color((double) 211 / 255, (double) 211 / 255, (double) 211 / 255)) );
      }

      messagesBox.getChildren().add(textLayoutBox);

    }

    messagesPane.setContent(messagesBox);

  }

}
