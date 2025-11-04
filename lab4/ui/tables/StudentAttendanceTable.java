package com.example.student_registry.ui.tables;

import com.example.student_registry.logic.*;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class StudentAttendanceTable extends StudentTable {

  public StudentAttendanceTable(){

    StudentAttendanceTableMenu studentAttendanceTableMenu = new StudentAttendanceTableMenu(
        this::updateFilter,
        this::updateEditMode,
        this::setGroupFilter,
        this::exportToPDF
    );
    vLayout.getChildren().add(studentAttendanceTableMenu);

  }

  @Override
  public void setGroupFilter(Group _group){
    group = _group;
    updateStudentsPane();
    updateAttendanceData();
    drawAttendance();
  }

  @Override
  public void update(UIUpdateType _updateType) {
    updateStudentsPane();
    updateAttendanceData();
    drawAttendance();
  }

  private final ArrayList<Pair<LocalDate, Boolean>[]> attendanceData = new ArrayList<>();
  private Boolean editMode = false;

  private void drawAttendance(){

    if(editMode){
      drawAttendanceEdit();
    }
    else {
      drawAttendanceNoEdit();
    }

  }
  private void drawAttendanceEdit(){

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    ArrayList<LocalDate> dates = new ArrayList<>();
    ArrayList<Label> dateLabels = new ArrayList<>();

    LocalDate startDate = this.startDate;

    while(!startDate.isAfter(endDate)){
      Label label = new Label(startDate.format(dateTimeFormatter));
      dateLabels.add(label);
      dates.add(startDate);
      startDate = startDate.plusDays(1);
    }
    addNodesToPane(studentPane, 0, 3, dateLabels.toArray(new Node[0]));

    int row = 1;
    for(int a = 0; a < students.size(); ++a){
      ArrayList<ComboBox<AttendanceType>> attendanceFields = new ArrayList<>();
      for(int i = 0; i < dates.size(); ++i){
        ComboBox<AttendanceType> attendanceField = new ComboBox<>();
        attendanceField.getItems().setAll(AttendanceType.values());

        // set the combo box value to what the attendance type is
        Pair<LocalDate, Boolean>[] studentAttendance = DB.getInstance().getAttendance(students.get(a), this.startDate, this.endDate);
        attendanceField.setValue(AttendanceType.None);
        if(studentAttendance != null){
          for(int b = 0; b < studentAttendance.length; ++b){
            if(studentAttendance[b].getKey().equals(dates.get(i))){
              if(studentAttendance[b].getValue()){
                attendanceField.setValue(AttendanceType.Present);
              }
              else {
                attendanceField.setValue(AttendanceType.Absent);
              }
              break;
            }
          }
        }

        // Can't pass mutable local variables to lambda... I miss c++
        int passA = a;
        int passI = i;
        attendanceField.setOnAction( e -> {
          DB.getInstance().updateAttendance(students.get(passA), dates.get(passI), attendanceField.getValue());
        });

        attendanceFields.add(attendanceField);
      }
      row = addNodesToPane(studentPane, row, 3, attendanceFields.toArray(new Node[0]));
    }

  }
  private void drawAttendanceNoEdit(){

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    ArrayList<LocalDate> dates = new ArrayList<>();
    ArrayList<Label> dateLabels = new ArrayList<>();

    LocalDate startDate = this.startDate;

    while(!startDate.isAfter(endDate)){
      Label label = new Label(startDate.format(dateTimeFormatter));
      dateLabels.add(label);
      dates.add(startDate);
      startDate = startDate.plusDays(1);
    }
    addNodesToPane(studentPane, 0, 3, dateLabels.toArray(new Node[0]));

    // draw attendance
    for(int i = 0; i < attendanceData.size(); ++i){
      if(attendanceData.get(i) == null){
        continue;
      }
      for(int j = 0; j < attendanceData.get(i).length; ++j){
        Pair<LocalDate, Boolean> attendance = attendanceData.get(i)[j];
        int daysBetween = (int)ChronoUnit.DAYS.between(this.startDate, attendance.getKey());
        Text attendanceText = new Text("");
        if(attendance.getValue()){
          attendanceText.setText("Present");
          attendanceText.setFill(Color.GREEN);
        }
        else {
          attendanceText.setText("Absent");
          attendanceText.setFill(Color.RED);
        }
        addNodesToPane(studentPane, i + 1, 3 + daysBetween, new Node[]{attendanceText});
      }
    }

  }

  private LocalDate startDate = LocalDate.of(1, 1, 1);
  private LocalDate endDate = LocalDate.of(1, 1, 1);
  private void updateFilter(LocalDate _startDate, LocalDate _endDate){
    startDate = _startDate;
    endDate = _endDate;
    updateStudentsPane();
    updateAttendanceData();
    drawAttendance();
  }
  private void updateAttendanceData(){

    attendanceData.clear();

    for(Student student: students){
      attendanceData.add(DB.getInstance().getAttendance(student, startDate, endDate));
    }

  }
  private void updateEditMode(Boolean value){
    editMode = value;
    updateStudentsPane();
    drawAttendance();
  }
  private void exportToPDF(){
    try {
      PDFSaver.export(scrollPane, "data.pdf");
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
