package com.example.online_store.logic.notification;

import com.example.online_store.logic.User;

public class NotificationEmail implements Notification {

  public NotificationEmail(User _user){
    user = _user;
  }

  @Override
  public void run(String _message) {
    user.getUIWindow().displayNotification(
        "Received email notification",
        _message
    );
  }

  private final User user;

}
