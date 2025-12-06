package com.example.lab6.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.function.BiConsumer;

public class Filter extends StackPane {

  public Filter(
    BiConsumer<LocalDate, LocalDate> _onConfirm,
    Runnable _onClearFilter
  ) {

    DatePicker startDate = new DatePicker();
    DatePicker endDate = new DatePicker();

    Button confirmButton = new Button("Filter by date");
    confirmButton.setOnAction( e -> _onConfirm.accept(startDate.getValue(), endDate.getValue()));

    Button clearFilterButton = new Button("Clear filter");
    clearFilterButton.setOnAction( e -> _onClearFilter.run());

    VBox layout = new VBox(startDate, endDate, confirmButton, clearFilterButton);
    layout.setAlignment(Pos.CENTER);

    this.getChildren().add(layout);

  }

}
