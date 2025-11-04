package com.example.online_store.logic.products.properties;

import com.example.online_store.logic.products.Product;
import javafx.util.Pair;

import java.util.ArrayList;

public abstract class ProductProperty extends Product {

  public ProductProperty(Product _product){
    product = _product;
  }

  @Override
  public String getName() {
    return product.getName();
  }

  @Override
  public String getDescription(){
    return product.getDescription();
  }

  @Override
  public double getPrice() {
    return product.getPrice();
  }

  @Override
  public String getProductType() { return product.getProductType(); }

  @Override
  public ArrayList<Pair<String, String>> getProperties() {
    return product.getProperties();
  }

  private Product product;

}
