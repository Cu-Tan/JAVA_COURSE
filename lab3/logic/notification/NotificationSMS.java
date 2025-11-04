package com.example.online_store.logic.notification;

import com.example.online_store.logic.User;

public class NotificationSMS implements Notification {

  public NotificationSMS(User _user){
    user = _user;
  }

  @Override
  public void run(String _message) {
    user.getUIWindow().displayNotification(
        "Received sms notification",
        _message
    );
  }

  private final User user;

}
