package com.example.lab6.ui;

import com.example.lab6.logic.CSVReaderRunnable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Root extends Application {
  @Override
  public void start(Stage stage) throws IOException {

    Scene scene = new Scene( new Pane(), 800, 600);

    DataTable dataTable1 = new DataTable();
    Text dateTable1ProgressText = new Text("Progress: 0%");
    Filter filter1 = new Filter(
      dataTable1::filterData,
      dataTable1::clearFilter
    );
    HBox layout1 = new HBox(dataTable1, dateTable1ProgressText, filter1);
    HBox.setHgrow(dataTable1, Priority.ALWAYS);

    DataTable dataTable2 = new DataTable();
    Text dateTable2ProgressText = new Text("Progress: 0%");
    Filter filter2 = new Filter(
      dataTable2::filterData,
      dataTable2::clearFilter
    );
    HBox layout2 = new HBox(dataTable2, dateTable2ProgressText, filter2);
    HBox.setHgrow(dataTable2, Priority.ALWAYS);

    DataTable dataTable3 = new DataTable();
    Text dateTable3ProgressText = new Text("Progress: 0%");
    Filter filter3 = new Filter(
      dataTable3::filterData,
      dataTable3::clearFilter
    );
    HBox layout3 = new HBox(dataTable3, dateTable3ProgressText, filter3);
    HBox.setHgrow(dataTable3, Priority.ALWAYS);

    VBox layout = new VBox(layout1, layout2, layout3);
    layout.setAlignment(Pos.CENTER);
    scene.setRoot(layout);

    Thread thread1 = new Thread(new CSVReaderRunnable(
      new File("MOCK_DATA1.csv"),
      (data) -> {
        Platform.runLater( () -> {
          dataTable1.addData(data);
        });
      },
      (progress) -> {
        Platform.runLater( () -> {
          dateTable1ProgressText.setText("Progress: " + String.format("%.2f%%", progress * 100));
        });
      }
    ));
    thread1.setDaemon(true);

    Thread thread2 = new Thread(new CSVReaderRunnable(
      new File("MOCK_DATA2.csv"),
      (data) -> {
        Platform.runLater( () -> {
          dataTable2.addData(data);
        });
      },
      (progress) -> {
        Platform.runLater( () -> {
          dateTable2ProgressText.setText("Progress: " + String.format("%.2f%%", progress * 100));
        });
      }
    ));
    thread2.setDaemon(true);

    Thread thread3 = new Thread(new CSVReaderRunnable(
      new File("MOCK_DATA3.csv"),
      (data) -> {
        Platform.runLater( () -> {
          dataTable3.addData(data);
        });
      },
      (progress) -> {
        Platform.runLater( () -> {
          dateTable3ProgressText.setText("Progress: " + String.format("%.2f%%", progress * 100));
        });
      }
    ));
    thread3.setDaemon(true);

    thread1.start();
    thread2.start();
    thread3.start();

    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();

  }
}
