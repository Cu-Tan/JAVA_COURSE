package com.example.online_store.ui.payment;

import com.example.online_store.logic.User;
import com.example.online_store.logic.products.Product;
import javafx.stage.Stage;

public abstract class PaymentWindow {

  public void show(){
    window.show();
  }

  protected Stage window;
  protected User user;
  protected Product product;

}
