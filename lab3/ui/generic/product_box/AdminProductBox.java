package com.example.online_store.ui.generic.product_box;

import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.products.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AdminProductBox extends StackPane {

  public AdminProductBox(Product _product){

    ProductInfoBox productInfoBox = new ProductInfoBox(_product);
    Button removeButton = new Button("Remove");

    removeButton.setOnAction( e -> {
      StoreController.getInstance().removeProduct(_product);
    });

    VBox vBox = new VBox(productInfoBox, removeButton);
    vBox.setAlignment(Pos.CENTER);

    this.getChildren().add(vBox);

    this.setBorder(Border.stroke(Color.BLACK));
    this.setPadding(new Insets(4, 4, 4, 4));

  }

}
