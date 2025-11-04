package com.example.online_store.logic;

import com.example.online_store.logic.notification.NotificationMethod;
import com.example.online_store.logic.payment.PaymentMethod;
import com.example.online_store.ui.window.UserWindow;

import java.util.ArrayList;

public class User {

  public User(){
    uiWindow = new UserWindow(this);
  }

  public ArrayList<NotificationMethod> notificationMethods = new ArrayList<>();

  public void addNotificationMethod(NotificationMethod _notificationMethod){
    if(notificationMethods.contains(_notificationMethod)){
      return;
    }
    notificationMethods.add(_notificationMethod);
  }
  public void removeNotificationMethod(NotificationMethod _notificationMethod){
    notificationMethods.remove(_notificationMethod);
  }

  public void setPaymentMethod(PaymentMethod _paymentMethod){
    paymentMethod = _paymentMethod;
  }
  public PaymentMethod getPaymentMethod(){ return paymentMethod; }

  public UserWindow getUIWindow(){
    return uiWindow;
  }

  private final UserWindow uiWindow;
  private PaymentMethod paymentMethod;
}
