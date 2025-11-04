package com.example.online_store.logic.products.properties.computer_properties;

import com.example.online_store.logic.products.Computer;
import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.properties.ProductProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class ComputerPropertyRAM extends ProductProperty {

  public ComputerPropertyRAM(Product _product, String _ram){
    super(_product);
    ram = _ram;
  }

  @Override
  public ArrayList<Pair<String, String>> getProperties() {
    ArrayList<Pair<String, String>> properties = super.getProperties();
    properties.add(new Pair<String, String>("RAM", ram));
    return properties;
  }

  private final String ram;

}
