package com.example.game.helper;

public class Line {

  public Coordinates start;
  public Coordinates end;

  public Line(Coordinates _start, Coordinates _end){
    this.start = _start;
    this.end = _end;
  }

  public double distance(){
    return Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
  }

}
