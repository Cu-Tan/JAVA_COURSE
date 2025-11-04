package com.example.online_store.logic.products;

public class ProductFactory {

  public static Product createProduct(ProductType _productType){
    switch (_productType){
      case TABLE -> {
        return new Table();
      }
      case COMPUTER -> {
        return new Computer();
      }
    }
    return null;
  }
}
