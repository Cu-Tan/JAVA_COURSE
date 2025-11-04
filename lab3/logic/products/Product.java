package com.example.online_store.logic.products;

import javafx.util.Pair;

import java.util.ArrayList;

public abstract class Product {

  public void setContent(
      String _name,
      String _description,
      double _price,
      ProductType _productType
  ) {
    this.name =  _name;
    this.description = _description;
    this.price = _price;
    productType = _productType;
  }
  public String getName(){
    return name;
  }
  public double getPrice(){
    return price;
  }
  public String getDescription(){
    return description;
  }
  public String getProductType() { return productType.name(); }
  public abstract ArrayList<Pair<String, String>> getProperties();

  protected String name;
  protected double price;
  protected String description;
  protected ProductType productType;
  protected ArrayList<Pair<String, String>> properties;

}
