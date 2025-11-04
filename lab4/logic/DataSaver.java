package com.example.student_registry.logic;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;

public class DataSaver {

  public static void save(String _filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(_filePath))) {

      saveGroups(writer);
      writer.newLine();

      saveStudents(writer);
      writer.newLine();

      saveAttendance(writer);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void saveGroups(BufferedWriter _writer) throws Exception {

    // Write headers
    _writer.write("id,name");
    _writer.newLine();

    // Write group data
    for(Group group: DB.getInstance().groups){
      _writer.write(group.ID + "," + group.name);
      _writer.newLine();
    }

  }

  private static void saveStudents(BufferedWriter _writer) throws Exception {

    // Write headers
    _writer.write("id,name,surname,group_id");
    _writer.newLine();

    // Write group data
    for(Student student: DB.getInstance().students){
      _writer.write((student.ID + "," + student.name + "," + student.surname + "," + ((student.groupID != null) ? student.groupID : "NONE")));
      _writer.newLine();
    }

  }

  private static void saveAttendance(BufferedWriter _writer) throws Exception {

    // Write headers
    _writer.write("student_id,attendance_data");
    _writer.newLine();

    DB db = DB.getInstance();


    // Write attendance data for each student
    for(Student student: DB.getInstance().students){

      StringBuilder sb = new StringBuilder();

      // get all attendance data
      Pair<LocalDate, Boolean>[] studentAttendanceData = db.getAttendance(student, null, null);

      // write all attendance data
      if(studentAttendanceData != null){
        for(int i = 0; i < studentAttendanceData.length; ++i){

          Pair<LocalDate, Boolean> attendanceData = studentAttendanceData[i];

          sb.append(attendanceData.getKey().toString()).append(",").append(attendanceData.getValue());

          if(i < studentAttendanceData.length - 1) {
            sb.append(",");
          }
        }

        _writer.write(student.ID.toString() + "," + sb);
        _writer.newLine();

      }
    }

  }

}
