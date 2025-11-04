package com.example.online_store.ui.generic.product_box;

import com.example.online_store.logic.User;
import com.example.online_store.logic.products.Product;
import com.example.online_store.ui.payment.PaymentWindow;
import com.example.online_store.ui.payment.PaymentWindowFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserProductBox extends StackPane {

  public UserProductBox(Product _product, User _user){

    ProductInfoBox productInfoBox = new ProductInfoBox(_product);
    Button buyButton = new Button("Buy");

    buyButton.setOnAction( e -> {

      // Don't want to TRY to buy anything if the user has not set any payment method
      if(_user.getPaymentMethod() == null){
        return;
      }

      PaymentWindow paymentWindow = PaymentWindowFactory.create(_user.getPaymentMethod(), _product, _user);
      paymentWindow.show();

    });

    VBox vBox = new VBox(productInfoBox, buyButton);
    vBox.setAlignment(Pos.CENTER);

    this.getChildren().add(vBox);

    this.setBorder(Border.stroke(Color.BLACK));
    this.setPadding(new Insets(4, 4, 4, 4));

  }

}
