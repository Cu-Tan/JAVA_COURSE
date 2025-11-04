package com.example.online_store.ui;

import com.example.online_store.logic.StoreController;
import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.ProductFactory;
import com.example.online_store.logic.products.ProductType;
import com.example.online_store.logic.products.properties.ProductPropertyType;
import com.example.online_store.logic.products.properties.PropertyFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class AdminProductCreationPane extends GridPane {

  public AdminProductCreationPane(){

    this.setVgap(8);
    this.setHgap(8);

    productTypeField.getItems().addAll(ProductType.values());
    productTypeField.setOnAction( e -> {
      propertyBoxes.clear();
      draw();
    });

    addPropertyButton.setOnAction( e -> addProperty());
    createProductButton.setOnAction( e -> createProduct());

    draw();

  }

  private final Label nameLabel = new Label("Name:");
  private final TextField nameField = new TextField();

  private final Label desciptionLabel = new Label("Description:");
  private final TextField descriptionField = new TextField();

  private final Label priceLabel = new Label("Price:");
  private final TextField priceField = new TextField();

  private final Label productTypeLabel = new Label("Product type:");
  private final ComboBox<ProductType> productTypeField = new ComboBox<>();

  private final Button addPropertyButton = new Button("Add property");
  private final ArrayList<PropertyBox> propertyBoxes = new ArrayList<>();
  private final Button createProductButton = new Button("Create product");

  private static class PropertyBox{

    public PropertyBox(ProductType _productType){

      ArrayList<ProductPropertyType> productPropertyTypes = new ArrayList<>(List.of(ProductPropertyType.values()));
      productPropertyTypes.removeIf(productPropertyType -> !productPropertyType.getProductClass().isAssignableFrom(_productType.getProductClass()));
      productPropertyTypeField.getItems().addAll(productPropertyTypes);

    }

    public Pair<ProductPropertyType, String> getData(){
      return new Pair<ProductPropertyType, String>(productPropertyTypeField.getValue(), productPropertyField.getText());
    }
    public void setOnDelete(Runnable _onDelete){

      deleteButton.setOnAction( e -> _onDelete.run());

    }
    public int draw(GridPane _root, int _row){

      _root.add(productPropertyTypeLabel, 1, _row);
      _root.add(productPropertyTypeField, 2, _row);

      ++_row;

      _root.add(productPropertyLabel, 1, _row);
      _root.add(productPropertyField, 2, _row);

      ++_row;

      _root.add(deleteButton, 1, _row);

      return _row + 1;

    }

    private final Label productPropertyTypeLabel = new Label("Product property:");
    private final ComboBox<ProductPropertyType> productPropertyTypeField = new ComboBox<>();

    private final Label productPropertyLabel = new Label("Property:");
    private final TextField productPropertyField = new TextField();

    private final Button deleteButton = new Button("X");

  }

  private void addProperty(){

    if(productTypeField.getValue() == null){
      return;
    }

    PropertyBox newPropertyBox = new PropertyBox(productTypeField.getValue());
    newPropertyBox.setOnDelete( () -> {
      propertyBoxes.remove(newPropertyBox);
      draw();
    } );
    propertyBoxes.add(newPropertyBox);

    draw();

  }

  private void createProduct(){

    if(productTypeField.getValue() == null){
      return;
    }

    Product newProduct = ProductFactory.createProduct(productTypeField.getValue());

    if(newProduct == null){
      return;
    }

    newProduct.setContent(
        nameField.getText(),
        descriptionField.getText(),
        Double.parseDouble(priceField.getText()),
        productTypeField.getValue()
    );

    for(PropertyBox propertyBox: propertyBoxes){
      Pair<ProductPropertyType, String> propertyData = propertyBox.getData();

      if(propertyData.getKey() == null){
        continue;
      }

      newProduct = PropertyFactory.create(propertyData.getKey(), newProduct, propertyData.getValue());
    }

    StoreController storeController = StoreController.getInstance();
    storeController.addProduct(newProduct);

  }

  private void draw(){

    this.getChildren().clear();

    this.add(nameLabel, 0, 0);
    this.add(nameField, 1, 0);

    this.add(desciptionLabel, 0, 1);
    this.add(descriptionField, 1, 1);

    this.add(priceLabel, 0, 2);
    this.add(priceField, 1, 2);

    this.add(productTypeLabel, 0, 3);
    this.add(productTypeField, 1, 3);

    this.add(addPropertyButton, 0, 4);

    int row = 5;

    for(PropertyBox propertyBox: propertyBoxes){
      row = propertyBox.draw(this, row);
    }

    this.add(createProductButton, 0, row);

  }
}
