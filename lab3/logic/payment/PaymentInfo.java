package com.example.online_store.logic.payment;

public class PaymentInfo{

  public PaymentInfo(String _info, PaymentMethod _paymentMethod){
    paymentMethod = _paymentMethod;
    info = _info;
  }

  public PaymentMethod paymentMethod;
  public String info;

}