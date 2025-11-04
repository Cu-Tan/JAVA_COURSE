package com.example.online_store.ui.generic.product_box;

import com.example.online_store.logic.products.Product;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;

public class ProductInfoBox extends GridPane {

  public ProductInfoBox(Product _product){

    this.setVgap(8);
    this.setHgap(8);

    Label nameLabel = new Label("Name:");
    Text nameText = new Text(_product.getName());

    Label descriptionLabel = new Label("Description:");
    Text descriptionText = new Text(_product.getDescription());

    Label typeLabel = new Label("Type:");
    Text typeText = new Text(_product.getProductType());

    Label propertyLabel = new Label("Properties:");
    Text propertyText = new Text();

    Label priceLabel = new Label("Price:");
    Text priceText = new Text(String.format("%.2f", _product.getPrice()));

    String propertyString = "";
    ArrayList<Pair<String, String>> productProperties = _product.getProperties();
    for(int i = 0; i < productProperties.size(); ++i){
      propertyString = propertyString + String.format("%s %c %s", productProperties.get(i).getKey(), PROPERTY_DELIMITER, productProperties.get(i).getValue());
      if(i != productProperties.size() - 1){
        propertyString = propertyString + "\n";
      }
    }

    propertyText.setText(propertyString);

    this.add(nameLabel, 0, 0);
    this.add(nameText, 1, 0);

    this.add(descriptionLabel, 0, 1);
    this.add(descriptionText, 1, 1);

    this.add(typeLabel, 0, 2);
    this.add(typeText, 1, 2);

    this.add(propertyLabel, 0, 3);
    this.add(propertyText, 1, 3);

    this.add(priceLabel, 0, 4);
    this.add(priceText, 1, 4);

  }

  private final char PROPERTY_DELIMITER = '-';

}
