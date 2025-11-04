package com.example.online_store.ui.payment;

import com.example.online_store.logic.User;
import com.example.online_store.logic.payment.PaymentMethod;
import com.example.online_store.logic.products.Product;

public class PaymentWindowFactory {

  public static PaymentWindow create(PaymentMethod _paymentMethod, Product _product, User _user){
    switch (_paymentMethod){
      case PAYPAL -> {
        return new PaymentWindowPaypal(_product, _user);
      }
      case CREDIT_CARD -> {
        return new PaymentWindowCreditCard(_product, _user);
      }
    }
    return null;
  }

}
