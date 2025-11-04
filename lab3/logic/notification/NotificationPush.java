package com.example.online_store.logic.notification;

import com.example.online_store.logic.User;

public class NotificationPush implements Notification {

  public NotificationPush(User _user){
    user = _user;
  }

  @Override
  public void run(String _message) {
    user.getUIWindow().displayNotification(
        "Received push notification",
        _message
    );
  }

  private final User user;

}
