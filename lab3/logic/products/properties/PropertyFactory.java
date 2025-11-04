package com.example.online_store.logic.products.properties;

import com.example.online_store.logic.products.Product;
import com.example.online_store.logic.products.properties.computer_properties.ComputerPropertyCPU;
import com.example.online_store.logic.products.properties.computer_properties.ComputerPropertyRAM;
import com.example.online_store.logic.products.properties.generic_properties.ProductPropertyHeight;
import com.example.online_store.logic.products.properties.generic_properties.ProductPropertyWidth;

public class PropertyFactory {

  public static Product create(ProductPropertyType _productPropertyType, Product _product, String _propertyValue){
    switch (_productPropertyType){
      case HEIGHT -> {
        return new ProductPropertyHeight(_product, _propertyValue);
      }
      case WIDTH -> {
        return new ProductPropertyWidth(_product, _propertyValue);
      }
      case CPU -> {
        return new ComputerPropertyCPU(_product, _propertyValue);
      }
      case RAM -> {
        return new ComputerPropertyRAM(_product, _propertyValue);
      }
    }
    return null;
  }

}
