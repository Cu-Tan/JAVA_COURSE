package com.example.online_store.logic;

import com.example.online_store.logic.products.Product;
import com.example.online_store.ui.generic.product_grid.ProductGrid;

import java.util.ArrayList;

public class ProductGridUpdater {

  // region Singleton
  public static ProductGridUpdater getInstance(){
    if(instance == null){
      instance = new ProductGridUpdater();
    }
    return instance;
  }
  private static ProductGridUpdater instance;
  // endregion

  public void update(){
    for(ProductGrid productGrid: productGrids){
      Product[] products = StoreController.getInstance().getProducts();
      productGrid.setProducts(products);
    }
  }

  public void subscribe(ProductGrid _productGrid){
    productGrids.add(_productGrid);
  }
  public void unsubscribe(ProductGrid _productGrid) { productGrids.remove(_productGrid); }

  private ArrayList<ProductGrid> productGrids = new ArrayList<>();

  private ProductGridUpdater(){};

}
