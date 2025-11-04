package com.example.online_store.ui.generic.product_grid;
import com.example.online_store.logic.products.Product;
import javafx.scene.layout.GridPane;

public abstract class ProductGrid extends GridPane {

  public ProductGrid(){

    this.setVgap(8);
    this.setHgap(8);

  }

  public void setProducts(Product[] _products){

    draw(_products);

  }

  protected final int MAX_IN_ROW = 3;

  protected void draw(Product[] _products){

    this.getChildren().clear();
    int row = 0;
    int col = 0;

    for(Product product: _products){

      if(col == MAX_IN_ROW){
        ++row;
        col = 0;
      }

      drawProductBox(product, col, row);

      ++col;

    }
  }
  protected abstract void drawProductBox(Product _product, int _col, int _row);

}
