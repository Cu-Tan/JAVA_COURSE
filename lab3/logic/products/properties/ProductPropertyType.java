package com.example.online_store.logic.products.properties;

import com.example.online_store.logic.products.Computer;
import com.example.online_store.logic.products.Product;

public enum ProductPropertyType {

  // Generic properties
  HEIGHT(Product.class),
  WIDTH(Product.class),

  // Computer specific properties
  CPU(Computer.class),
  RAM(Computer.class);

  ProductPropertyType(Class<? extends Product> _productClass){
    productClass = _productClass;
  }

  public Class<? extends Product> getProductClass(){
    return productClass;
  }

  private final Class<? extends Product> productClass;

}
