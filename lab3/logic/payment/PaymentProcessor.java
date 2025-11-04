package com.example.online_store.logic.payment;

import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.User;
import com.example.online_store.logic.products.Product;

public class PaymentProcessor {

  // region SINGLETON
  private static PaymentProcessor instance;
  public static PaymentProcessor getInstance(){
    if(instance == null){
      instance = new PaymentProcessor();
    }
    return instance;
  }
  private PaymentProcessor(){}
  // endregion

  public void processPayment(Product _product, User _user, PaymentInfo _paymentInfo){

    StoreController.getInstance().purchaseProduct(_product, _user, _paymentInfo);

  }

}
