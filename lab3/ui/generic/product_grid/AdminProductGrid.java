package com.example.online_store.ui.generic.product_grid;

import com.example.online_store.logic.ProductGridUpdater;
import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.products.Product;
import com.example.online_store.ui.generic.product_box.AdminProductBox;

public class AdminProductGrid extends ProductGrid {

  public AdminProductGrid(){
    setProducts(StoreController.getInstance().getProducts());
    ProductGridUpdater.getInstance().subscribe(this);
  }

  @Override
  protected void drawProductBox(Product _product, int _col, int _row) {

    AdminProductBox productBox = new AdminProductBox(_product);
    this.add(productBox, _col, _row);

  }
}
