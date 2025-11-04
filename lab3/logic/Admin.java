package com.example.online_store.logic;

import com.example.online_store.ui.window.AdminWindow;

public class Admin {

  public void showAdminWindow(){
    if(!adminWindow.isShowing()){
      adminWindow.show();
    }
  }

  public void displayNotification(String _message){
    adminWindow.displayNotification(_message);
  }

  private final AdminWindow adminWindow = new AdminWindow();

}
