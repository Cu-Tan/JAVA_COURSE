package com.example.game.helper;


/**
 * Class used for helper math functions
 */
public class CTMath {

  /**
   * Finds the intersection point of 2 lines
   */
  public static Coordinates lineIntersection(Line line1, Line line2){

    /*
      Need to calculate weather 2 lines intersect by finding A and B in these equations
      #s and #e mean start and end of # line

      All points on first line (0 < A < 1)
      x = x(1s) + A(x(1e) - x(1s))
      y = y(1s) + A(y(1e) - y(1s))

      All points on second line (0 < B < 1)
      x = x(2s) + B(x(2e) - x(2s))
      y = y(2s) + B(y(2e) - y(2s))

      Final formular when solving

      let dx# = x(#e) - x(#s)
      let dy# = y(#e) - y(#s)

      let dx21 = x(2s) - x(1s)
      let dy21 = y(2s) - y(1s)

      A = (dx21 * dy2 - dy21 * dx2) / (dx1 * dy2 - dy1 * dx2)
      B = (dx21 * dy1 - dy21 * dx1) / (dx1 * dy2 - dy1 * dx2)

      let det = (dx1 * dy2 - dy1 * dx2)

     */

    double dx1 = line1.end.x - line1.start.x;
    double dy1 = line1.end.y - line1.start.y;

    double dx2 = line2.end.x - line2.start.x;
    double dy2 = line2.end.y - line2.start.y;

    double dx21 = line2.start.x - line1.start.x;
    double dy21 = line2.start.y - line1.start.y;

    double det = dx1 * dy2 - dy1 * dx2;

    // Lines are parallel and not collinear
    if(Math.abs(det) < 1e-9 ){
      return null;
    }

    double A = (dx21 * dy2 - dy21 * dx2) / det;

    // Lines do not intersect withing the given points
    if(A < 0 || A > 1){
      return null;
    }

    double B = (dx21 * dy1 - dy21 * dx1) / det;

    // Lines do not intersect withing the given points
    if(B < 0 || B > 1){
      return null;
    }

    return new Coordinates(
        line1.start.x + A * (line1.end.x - line1.start.x),
        line1.start.y + A * (line1.end.y - line1.start.y)
    );
  }
}
