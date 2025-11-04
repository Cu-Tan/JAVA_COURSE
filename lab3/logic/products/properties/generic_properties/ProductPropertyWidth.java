package com.example.online_store.logic.products.properties.generic_properties;

import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.properties.ProductProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class ProductPropertyWidth extends ProductProperty {

  public ProductPropertyWidth(Product _product, String _width){
    super(_product);
    width = _width;
  }

  @Override
  public ArrayList<Pair<String, String>> getProperties() {
    ArrayList<Pair<String, String>> properties = super.getProperties();
    properties.add(new Pair<>("Width", width));
    return properties;
  }

  private final String width;

}