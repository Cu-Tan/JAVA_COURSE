package com.example.student_registry.logic;

import javafx.util.Pair;
import java.time.LocalDate;
import java.util.UUID;

public final class Student {

  public Student(String _name, String _surname, UUID _groupID){
    this(_name, _surname, _groupID, UUID.randomUUID());
  }
  public Student(String _name, String _surname, UUID _groupID, UUID _id){
    ID = _id;
    name = _name;
    surname = _surname;
    groupID = _groupID;
  }

  public UUID ID;
  public String name;
  public String surname;
  public UUID groupID;

  @Override
  public String toString(){
    return ID + " | " + name + " | " + surname + " | " + groupID;
  }

}
