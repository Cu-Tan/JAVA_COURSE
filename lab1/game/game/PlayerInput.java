package com.example.game.game;

import javafx.scene.input.KeyEvent;

public class PlayerInput {

  private PlayerInput() {}

  public static boolean UP_PRESSED;
  public static boolean RIGHT_PRESSED;
  public static boolean LEFT_PRESSED;
  public static boolean DOWN_PRESSED;
  public static boolean SPACE_PRESSED;

  public static void handle(KeyEvent _event){
    if(_event.getEventType() == KeyEvent.KEY_PRESSED){
      switch (_event.getCode()){
        case W -> {
          UP_PRESSED = true;
        }
        case S -> {
          DOWN_PRESSED = true;
        }
        case D -> {
          RIGHT_PRESSED = true;
        }
        case A -> {
          LEFT_PRESSED = true;
        }
        case SPACE -> {
          SPACE_PRESSED = true;
        }
      }
    }
    else if (_event.getEventType() == KeyEvent.KEY_RELEASED){
      switch (_event.getCode()){
        case W -> {
          UP_PRESSED = false;
        }
        case S -> {
          DOWN_PRESSED = false;
        }
        case D -> {
          RIGHT_PRESSED = false;
        }
        case A -> {
          LEFT_PRESSED = false;
        }
        case SPACE -> {
          SPACE_PRESSED = false;
        }
      }
    }
  }

  public static void resetInput(){
    UP_PRESSED = false;
    RIGHT_PRESSED = false;
    LEFT_PRESSED = false;
    DOWN_PRESSED = false;
    SPACE_PRESSED = false;
  }

}
