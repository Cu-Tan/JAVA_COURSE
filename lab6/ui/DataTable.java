package com.example.lab6.ui;

import com.example.lab6.logic.Data;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class DataTable extends TableView<Data> {

  public DataTable(){

    TableColumn<Data, Number> idCol = createCustomColumn(
        "ID",
        Data::getID,
        new PropertyValueFactory<>("ID")
    );

    TableColumn<Data, String> firstNameCol = createCustomColumn(
        "First Name",
        Data::getFirstName,
        new PropertyValueFactory<>("firstName")
    );

    TableColumn<Data, String> lastNameCol = createCustomColumn(
        "Last Name",
        Data::getLastName,
        new PropertyValueFactory<>("lastName")
    );

    TableColumn<Data, String> emailCol = createCustomColumn(
        "Email",
        Data::getEmail,
        new PropertyValueFactory<>("email")
    );

    TableColumn<Data, String> genderCol = createCustomColumn(
        "Gender",
        Data::getGender,
        new PropertyValueFactory<>("gender")
    );

    TableColumn<Data, String> countryCol = createCustomColumn(
        "Country",
        Data::getCountry,
        new PropertyValueFactory<>("country")
    );

    TableColumn<Data, String> domainCol = createCustomColumn(
        "Domain",
        Data::getDomainName,
        new PropertyValueFactory<>("domainName")
    );

    TableColumn<Data, String> dateOfBirthCol = createCustomColumn(
        "Date Of Birth",
        Data::getDateOfBirth,
        new PropertyValueFactory<>("dateOfBirth")
    );

    this.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, genderCol, countryCol, domainCol, dateOfBirthCol);

  }

  public void addData(
    Data[] _data
  ) {

    originalData.addAll( Arrays.stream(_data).toList() );
    this.getItems().addAll(_data);

  }

  public void filterData(
    LocalDate startDate,
    LocalDate endDate
  ) {

    List<Data> filteredData = originalData.stream().filter( data -> {

      LocalDate date = LocalDate.parse(data.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      return date.isAfter(startDate.minusDays(1)) && date.isBefore(endDate.plusDays(1));

    }).toList();

    this.getItems().setAll(filteredData);

  }

  public void clearFilter(){
    this.getItems().setAll(originalData);
  }

  private final ArrayList<Data> originalData = new ArrayList<>();

  /**
   * Helper function that creates a column for TableView in such a way that it disables built in sorting
   * and replaces it with custom sorting using streamAPI
   * @param title
   * @param extractor used to get the desired object from data
   * @param cellFactory tableView specific factory
   * @return the TableColumn for this TableView
   * @param <T> the specified data object type (egz. lastName -> String)
   * @param <C> required for extractor in the case of Integer because TableView reserves T as Number but comparable is received as Integer
   */
  private <T, C extends Comparable<C>> TableColumn<Data, T> createCustomColumn(
      String title,
      Function<Data, C> extractor,
      Callback<TableColumn.CellDataFeatures<Data, T>, ObservableValue<T>> cellFactory
  ) {
    TableColumn<Data, T> col = new TableColumn<>();
    col.setCellValueFactory(cellFactory);
    col.setSortable(false); // disable default sorting

    Label header = new Label(title);
    header.setMaxWidth(Double.MAX_VALUE);
    header.setMaxHeight(Double.MAX_VALUE);
    col.setGraphic(header);

    header.setOnMousePressed( e -> {
      header.setBackground( new Background( new BackgroundFill(Color.GREY, null, null) ) );
    });
    header.setOnMouseReleased( e -> {
      header.setBackground( new Background( new BackgroundFill(Color.TRANSPARENT, null, null) ) );
    });

    // Lambda does not like mutable variables so this array will have to do because I don't want to use AtomicBoolean :/
    boolean[] ascending = new boolean[]{false};
    header.setOnMouseClicked(e -> {

      ascending[0] = !ascending[0];

      Comparator<Data> cmp = Comparator.comparing(extractor);
      if (!ascending[0]) cmp = cmp.reversed();

      List<Data> sorted = this.getItems()
          .stream()
          .sorted(cmp)
          .toList();

      this.getItems().setAll(sorted);

    });

    return col;
  }



}
