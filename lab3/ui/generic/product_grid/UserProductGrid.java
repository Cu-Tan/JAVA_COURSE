package com.example.online_store.ui.generic.product_grid;

import com.example.online_store.logic.ProductGridUpdater;
import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.User;
import com.example.online_store.logic.products.Product;
import com.example.online_store.ui.generic.product_box.UserProductBox;

public class UserProductGrid extends ProductGrid {

  public UserProductGrid(User _user){
    user = _user;

    setProducts(StoreController.getInstance().getProducts());
    ProductGridUpdater.getInstance().subscribe(this);
  }

  @Override
  protected void drawProductBox(Product _product, int _col, int _row) {

    UserProductBox productBox = new UserProductBox(_product, user);
    this.add(productBox, _col, _row);

  }

  private final User user;

}
