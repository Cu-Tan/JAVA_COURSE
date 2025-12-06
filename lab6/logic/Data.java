package com.example.lab6.logic;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Data {

  public Data(
    int _ID,
    String _firstName,
    String _lastName,
    String _email,
    String _gender,
    String _country,
    String _domainName,
    String _dateOfBirth
  ) {
    ID = new SimpleIntegerProperty(_ID);
    firstName = new SimpleStringProperty(_firstName);
    lastName = new SimpleStringProperty(_lastName);
    email = new SimpleStringProperty(_email);
    gender = new SimpleStringProperty(_gender);
    country = new SimpleStringProperty(_country);
    domainName = new SimpleStringProperty(_domainName);
    dateOfBirth = new SimpleStringProperty(_dateOfBirth);
  }

  public int getID() { return ID.get(); }
  public String getFirstName() { return firstName.get(); }
  public String getLastName() { return lastName.get(); }
  public String getEmail() { return email.get(); }
  public String getGender() { return gender.get(); }
  public String getCountry() { return country.get(); }
  public String getDomainName() { return domainName.get(); }
  public String getDateOfBirth() { return dateOfBirth.get(); }

  private final SimpleIntegerProperty ID;
  private final SimpleStringProperty firstName;
  private final SimpleStringProperty lastName;
  private final SimpleStringProperty email;
  private final SimpleStringProperty gender;
  private final SimpleStringProperty country;
  private final SimpleStringProperty domainName;
  private final SimpleStringProperty dateOfBirth;

}
