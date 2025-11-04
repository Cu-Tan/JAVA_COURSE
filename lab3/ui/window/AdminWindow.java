package com.example.online_store.ui.window;

import com.example.online_store.logic.ProductGridUpdater;
import com.example.online_store.ui.AdminProductCreationPane;
import com.example.online_store.ui.generic.product_grid.AdminProductGrid;
import com.example.online_store.ui.generic.product_grid.ProductGrid;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminWindow extends Stage {

  public AdminWindow(){

    AdminProductCreationPane adminsProductCreationPane = new AdminProductCreationPane();
    ProductGrid productGrid = new AdminProductGrid();

    ProductGridUpdater.getInstance().subscribe(productGrid);

    HBox hbox = new HBox(adminsProductCreationPane, productGrid, notificationText);
    hbox.setSpacing(16);

    Scene scene = new Scene(hbox, 1280, 720);

    this.setScene(scene);

  }

  public void displayNotification(String _message){
    notificationText.setText(_message);
  }

  private final Text notificationText = new Text("");

}
