package com.example.student_registry.logic;

import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Singleton Class that imitates a DB
 * Stores Student data
 * Stores Group data
 */
public final class DB {

  // region Singleton

  public static DB getInstance(){
    if(instance == null){
      instance = new DB();
    }
    return instance;
  }
  private static DB instance;
  private DB(){}

  // endregion

  public ArrayList<Student> students = new ArrayList<>();
  public ArrayList<Group> groups = new ArrayList<>();
  public ArrayList<Pair<Student, ArrayList<Pair<LocalDate, Boolean>>>> attendanceData = new ArrayList<>();

  public void addStudent(Student _student){
    students.add(_student);
    UIUpdater.getInstance().update(UIUpdateType.STUDENT);
  }
  public void editStudent(Student _oldStudent, Student _newStudent){
    _oldStudent.name = _newStudent.name;
    _oldStudent.surname = _newStudent.surname;
    _oldStudent.groupID = _newStudent.groupID;
    UIUpdater.getInstance().update(UIUpdateType.STUDENT);
  }
  public Student getStudentById(UUID _id){
    for(Student student: students){
      if(student.ID.equals(_id)){
        return student;
      }
    }
    return null;
  }
  public Student[] getStudentsByGroupID(UUID _groupID){

    ArrayList<Student> returnStudents = new ArrayList<>();

    for(Student student: students){
      if(student.groupID.equals(_groupID)){
        returnStudents.add(student);
      }
    }

    return returnStudents.toArray(new Student[0]);
  }

  public void addGroup(Group _group){

    groups.add(_group);
    UIUpdater.getInstance().update(UIUpdateType.GROUP);

  }
  public void editGroup(Group _oldGroup, Group _newGroup){

    _oldGroup.name = _newGroup.name;
    UIUpdater.getInstance().update(UIUpdateType.GROUP);

  }
  public Group getGroupByID(UUID _ID){
    for (Group group : groups) {
      if (group.ID.equals(_ID)) {
        return group;
      }
    }
    return null;
  }

  public void clearData(){
    students.clear();
    groups.clear();
    attendanceData.clear();
  }

  public void updateAttendance(Student _student, LocalDate _date, AttendanceType attendanceValue){

    Pair<Student, ArrayList<Pair<LocalDate, Boolean>>> studentAttendance = null;
    for(int i = 0; i < attendanceData.size(); ++i){
      if(attendanceData.get(i).getKey() == _student){
        studentAttendance = attendanceData.get(i);
        break;
      }
    }

    // If we found no student, and we say do nothing then do nothing
    if(studentAttendance == null && attendanceValue == AttendanceType.None){
      return;
    }

    // If no data found we need to create object to store it
    if(studentAttendance == null){
      studentAttendance = new Pair<Student, ArrayList<Pair<LocalDate, Boolean>>>(_student, new ArrayList<>());
      attendanceData.add(studentAttendance);
    }

    boolean found = false;
    Pair<LocalDate, Boolean> attendance = null;
    // Find the exact local date data inside studentAttendance
    for(int i = 0; i < studentAttendance.getValue().size(); ++i){
      attendance = studentAttendance.getValue().get(i);
      if(attendance.getKey().equals(_date)){
        found = true;
        break;
      }
    }

    if(attendance == null && attendanceValue == AttendanceType.None){
      return;
    }

    if(found){
      // Updating attendance value
      switch (attendanceValue){
        case None -> {
          studentAttendance.getValue().remove(attendance);
        }
        case Present -> {
          studentAttendance.getValue().remove(attendance);
          attendance = new Pair<>(_date, true);
          studentAttendance.getValue().add(attendance);
        }
        case Absent -> {
          studentAttendance.getValue().remove(attendance);
          attendance = new Pair<>(_date, false);
          studentAttendance.getValue().add(attendance);
        }
      }
    }
    else {
      // Adding attendance value
      switch (attendanceValue){
        case None -> {}
        case Present -> {
          studentAttendance.getValue().add(new Pair<>(_date, true));
        }
        case Absent -> {
          studentAttendance.getValue().add(new Pair<>(_date, false));
        }
      }
    }

    UIUpdater.getInstance().update(UIUpdateType.ATTENDANCE);

  }
  public Pair<LocalDate, Boolean>[] getAttendance(Student _student, LocalDate _startDate, LocalDate _endDate){

    ArrayList<Pair<LocalDate, Boolean>> attendanceData = new ArrayList<>();

    // Find the specific student attendance data
    ArrayList<Pair<LocalDate, Boolean>> studentAttendanceData = null;
    for(int i = 0; i < this.attendanceData.size(); ++i){
      if(this.attendanceData.get(i).getKey() == _student){
        studentAttendanceData = this.attendanceData.get(i).getValue();
        break;
      }
    }

    if(studentAttendanceData == null){
      return null;
    }

    // Filter from startDate to endDate (if null return all)
    for(Pair<LocalDate, Boolean> attendance: studentAttendanceData){

      if(_startDate == null && _endDate == null) {
        attendanceData.add(attendance);
        continue;
      }

      if(attendance.getKey().isAfter(_startDate.minusDays(1)) && attendance.getKey().isBefore(_endDate.plusDays(1))){
        attendanceData.add(attendance);
      }
    }

    return attendanceData.toArray(new Pair[0]);
  }
}
