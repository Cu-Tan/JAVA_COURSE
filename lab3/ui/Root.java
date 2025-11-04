package com.example.online_store.ui;

import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.User;
import com.example.online_store.ui.window.UserWindow;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Root extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    storeController = StoreController.getInstance();
    storeController.createAdmin(); // Very ugly workaround for admin circular dependency with store controller if instantiated inside constructor

    Button adminPanelButton = new Button("Open admin panel");
    Button userPanelButton = new Button("Open new user panel");

    VBox vbox = new VBox(adminPanelButton, userPanelButton);
    vbox.setAlignment(Pos.CENTER);

    adminPanelButton.setOnAction( e -> {
      storeController.admin.showAdminWindow();
    });

    userPanelButton.setOnAction( e -> {
      User newUser = new User();
      storeController.addUser(newUser);
      UserWindow newUserWindow = newUser.getUIWindow();
      newUserWindow.setOnCloseRequest( event -> {
        storeController.removeUser(newUser);
      });
      newUserWindow.show();
    });

    Scene scene = new Scene(vbox, 1280, 720);
    stage.setScene(scene);
    stage.show();
  }

  private StoreController storeController;

}
