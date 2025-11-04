package com.example.student_registry.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;
import java.time.LocalDate;
import java.util.UUID;

public class DataLoader {

  public static void load(String _filePath){

    DB.getInstance().clearData();

    try (BufferedReader reader = new BufferedReader(new FileReader(_filePath))) {

      loadGroups(reader);
      loadStudents(reader);
      loadAttendance(reader);

    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    UIUpdater.getInstance().update(UIUpdateType.ALL);

  }

  private static void loadGroups(BufferedReader reader) throws Exception {

    // Skip headers
    String line = reader.readLine();

    // read data
    line = reader.readLine();

    while(!line.isBlank()){

      // Load groupID and name
      String[] groupData = line.split(",");

      Group group = new Group(groupData[1], UUID.fromString(groupData[0]));
      DB.getInstance().addGroup(group);

      line = reader.readLine();
    }

  }
  private static void loadStudents(BufferedReader reader) throws Exception {

    // Skip headers
    String line = reader.readLine();

    // read data
    line = reader.readLine();

    while(!line.isBlank()){

      // Load ID, name, surname, groupID
      String[] studentData = line.split(",");

      Student student = new Student(
          studentData[1],
          studentData[2],
          UUID.fromString(studentData[3]),
          UUID.fromString(studentData[0])
      );
      DB.getInstance().addStudent(student);

      line = reader.readLine();
    }

  }
  private static void loadAttendance(BufferedReader reader) throws Exception {

    // Skip headers
    String line = reader.readLine();

    // read data
    line = reader.readLine();

    while(line != null){

      // Load StudentID, pairs of date and boolean
      String[] attendanceData = line.split(",");

      Student student = DB.getInstance().getStudentById(UUID.fromString(attendanceData[0]));

      for(int i = 1; i < attendanceData.length; i += 2){
        LocalDate date = LocalDate.parse(attendanceData[i]);
        boolean attendance = Boolean.parseBoolean(attendanceData[i + 1]);

        // Can use this function to add attendance since it doesn't duplicate it will just add missing data
        DB.getInstance().updateAttendance(
            student,
            date,
            (attendance) ? AttendanceType.Present : AttendanceType.Absent
        );

      }

      line = reader.readLine();
    }

  }

}
