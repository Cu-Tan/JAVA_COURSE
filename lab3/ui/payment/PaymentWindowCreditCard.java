package com.example.online_store.ui.payment;

import com.example.online_store.logic.User;
import com.example.online_store.logic.payment.PaymentInfo;
import com.example.online_store.logic.payment.PaymentMethod;
import com.example.online_store.logic.payment.PaymentProcessor;
import com.example.online_store.logic.products.Product;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentWindowCreditCard extends PaymentWindow {

  public PaymentWindowCreditCard(Product _product, User _user){

    product = _product;
    user = _user;
    window = new Stage();

    Label creditCardNumberLabel = new Label("Credit card number:");
    TextField creditCardNumberField = new TextField("");
    HBox hBox = new HBox(creditCardNumberLabel, creditCardNumberField);
    hBox.setAlignment(Pos.CENTER);

    Button payButton = new Button("Pay");

    payButton.setOnAction( e -> {
      PaymentProcessor.getInstance().processPayment(
          _product,
          _user,
          new PaymentInfo(
              creditCardNumberField.getText(),
              PaymentMethod.CREDIT_CARD
          )
      );
      window.close();
    });

    VBox vBox = new VBox(hBox, payButton);
    vBox.setAlignment(Pos.CENTER);

    Scene scene = new Scene(vBox, 1280, 720);

    window.setScene(scene);

  }

}
