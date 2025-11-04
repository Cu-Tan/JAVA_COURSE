package com.example.student_registry.logic;

import java.util.UUID;

public final class Group {

  public Group(String _name){
    this(_name, UUID.randomUUID());
  }
  public Group(String _name, UUID _id){
    ID = _id;
    name = _name;
  }


  public UUID ID;
  public String name;

  @Override
  public String toString(){
    return ID + " | " + name;
  }

}
