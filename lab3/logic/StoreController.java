package com.example.online_store.logic;

import com.example.online_store.logic.notification.Notification;
import com.example.online_store.logic.notification.NotificationFactory;
import com.example.online_store.logic.payment.PaymentInfo;
import com.example.online_store.logic.products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that is responsible for holding store data and actions
 */
public class StoreController {

  // region Singleton
  public static StoreController getInstance(){
    if(instance == null){
      instance = new StoreController();
    }
    return instance;
  }
  private static StoreController instance;
  // endregion

  public Admin admin;

  public void createAdmin(){
    admin = new Admin();
  }

  public void addProduct(Product _product){

    ArrayList<Product> productsOfType = products.get(_product.getClass());
    if(productsOfType == null){
      productsOfType = new ArrayList<>();
    }
    productsOfType.add(_product);
    products.put(_product.getClass(), productsOfType);

    ProductGridUpdater.getInstance().update();

    notifyUsers("A new product has been added!");
  }
  public void removeProduct(Product _product){

    ArrayList<Product> productsOfType = products.get(_product.getClass());
    if(productsOfType == null){
      return;
    }
    productsOfType.removeIf( product -> product == _product);

    ProductGridUpdater.getInstance().update();

    notifyUsers("A product has been removed!");
  }
  public Product[] getProducts(){

    ArrayList<Product> returnProducts = new ArrayList<>();
    for(Map.Entry<Class<? extends Product>, ArrayList<Product>> productOfTypeList: products.entrySet()){
      returnProducts.addAll(productOfTypeList.getValue());
    }
    return returnProducts.toArray(new Product[0]);

  }

  public void addUser(User _user){
    users.add(_user);
  }
  public void removeUser(User _user){
    ProductGridUpdater.getInstance().unsubscribe(_user.getUIWindow().productGrid);
    users.remove(_user);
  }

  public void purchaseProduct(Product _product, User _user, PaymentInfo _paymentInfo){

    String message = "Product " + _product.getName() + " has been purchased by user " + _user.toString() + '\n';

    switch (_paymentInfo.paymentMethod){
      case PAYPAL -> {
        message = message + "Payment method: PAYPAL";
      }
      case CREDIT_CARD -> {
        message = message + "Payment method: CREDIT CARD";
      }
    }

    message = message + " | Payment info: " + _paymentInfo.info;

    admin.displayNotification(message);

    removeProduct(_product);

  }


  private final ArrayList<User> users = new ArrayList<>();
  private final HashMap<Class<? extends Product>, ArrayList<Product>> products = new HashMap<>();

  private void notifyUsers(String _message){
    for(User user: users){
      Notification notification = NotificationFactory.create(user);
      notification.run(_message);
    }
  }

  private StoreController(){}

}
