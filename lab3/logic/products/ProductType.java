package com.example.online_store.logic.products;

public enum ProductType {
  TABLE(Table.class),
  COMPUTER(Computer.class);

  ProductType(Class<? extends Product> _productClass){
    productClass = _productClass;
  }

  public Class<? extends Product> getProductClass(){
    return productClass;
  }

  private final Class<? extends Product> productClass;

}
