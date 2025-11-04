package com.example.online_store.logic.notification;

import com.example.online_store.logic.User;

import java.util.ArrayList;

public class NotificationFactory {

  public static Notification create(User _user){

    ArrayList<Notification> notifications = new ArrayList<>();

    for(NotificationMethod notificationMethod: _user.notificationMethods){
      switch (notificationMethod){
        case SMS -> {
          notifications.add(
              new NotificationSMS(_user)
          );
        }
        case EMAIL -> {
          notifications.add(
              new NotificationEmail(_user)
          );
        }
        case PUSH -> {
          notifications.add(
              new NotificationPush(_user)
          );
        }
      }
    }

    return new MultiNotification(notifications.toArray(new Notification[0]));

  }
}
