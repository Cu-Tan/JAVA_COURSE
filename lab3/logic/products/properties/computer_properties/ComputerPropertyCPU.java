package com.example.online_store.logic.products.properties.computer_properties;

import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.properties.ProductProperty;
import javafx.util.Pair;

import java.util.ArrayList;

public class ComputerPropertyCPU extends ProductProperty {

  public ComputerPropertyCPU(Product _product, String _cpu){
    super(_product);
    cpu = _cpu;
  }

  @Override
  public ArrayList<Pair<String, String>> getProperties() {
    ArrayList<Pair<String, String>> properties = super.getProperties();
    properties.add(new Pair<String, String>("CPU", cpu));
    return properties;
  }

  private final String cpu;

}
