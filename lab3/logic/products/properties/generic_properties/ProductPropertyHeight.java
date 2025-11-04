package com.example.online_store.logic.products.properties.generic_properties;

import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.properties.ProductProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class ProductPropertyHeight extends ProductProperty {

  public ProductPropertyHeight(Product _product, String _height){
    super(_product);
    height = _height;
  }

  @Override
  public ArrayList<Pair<String, String>> getProperties() {
    ArrayList<Pair<String, String>> properties = super.getProperties();
    properties.add(new Pair<>("Height", height));
    return properties;
  }

  private final String height;

}
