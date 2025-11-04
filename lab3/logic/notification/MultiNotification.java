package com.example.online_store.logic.notification;

public class MultiNotification implements Notification {

  public MultiNotification(Notification[] _notifications){
    notifications = _notifications;
  }

  @Override
  public void run(String _message) {
    for(Notification notification: notifications){
      notification.run(_message);
    }
  }

  private final Notification[] notifications;
}
