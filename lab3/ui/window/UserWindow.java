package com.example.online_store.ui.window;

import com.example.online_store.logic.notification.NotificationMethod;
import com.example.online_store.logic.User;
import com.example.online_store.logic.payment.PaymentMethod;
import com.example.online_store.ui.generic.product_grid.ProductGrid;
import com.example.online_store.ui.generic.product_grid.UserProductGrid;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserWindow extends Stage {

  public UserWindow(User _user){

    user = _user;

    productGrid = new UserProductGrid(_user);

    addSubscriptionButtons();

    ComboBox<PaymentMethod> paymentMethodField = new ComboBox<>();
    paymentMethodField.getItems().addAll(PaymentMethod.values());
    paymentMethodField.setOnAction( e -> {
      _user.setPaymentMethod(paymentMethodField.getValue());
    });

    HBox hbox = new HBox(subscriptionBox, notificationBox, paymentMethodField);

    VBox vBox = new VBox(hbox, productGrid);

    ScrollPane scrollPane = new ScrollPane(vBox);

    Scene scene = new Scene(scrollPane, 1280, 720);
    this.setScene(scene);

  }

  public ProductGrid productGrid;

  public void addSubscriptionButtons(){

    subscriptionBox.getChildren().clear();

    for(NotificationMethod notificationMethod: NotificationMethod.values()){
      addSubscriptionButton(notificationMethod);
    }

  }
  public void addSubscriptionButton(NotificationMethod _notificationMethod){

    GridPane subscriptionMethodBox = new GridPane();

    Text notificationMethodText = new Text(_notificationMethod.name() + " notifications");
    Text notificationSubsciptionStatusText = new Text("Not subscribed");
    notificationSubsciptionStatusText.setFill(Color.RED);

    Button subscribeButton = new Button("Subscribe");
    Button unsubscribeButton = new Button("Unsubscribe");

    subscribeButton.setOnAction( e -> {
      user.addNotificationMethod(_notificationMethod);
      notificationSubsciptionStatusText.setText("Subscribed");
      notificationSubsciptionStatusText.setFill(Color.GREEN);
    });

    unsubscribeButton.setOnAction( e -> {
      user.removeNotificationMethod(_notificationMethod);
      notificationSubsciptionStatusText.setText("Not subscribed");
      notificationSubsciptionStatusText.setFill(Color.RED);
    });

    subscriptionMethodBox.add(notificationMethodText, 0, 0);
    subscriptionMethodBox.add(notificationSubsciptionStatusText, 1, 0);
    subscriptionMethodBox.add(subscribeButton, 0, 1);
    subscriptionMethodBox.add(unsubscribeButton, 1, 1);

    subscriptionBox.getChildren().add(subscriptionMethodBox);

  }

  private final VBox subscriptionBox = new VBox();
  private final VBox notificationBox = new VBox();

  private final User user;

  public void displayNotification(String header, String message){

    Text headerText = new Text(header);
    Text messageText = new Text(message);

    HBox hbox = new HBox(headerText, messageText);
    hbox.setSpacing(16);
    notificationBox.getChildren().add(hbox);

  }

}
