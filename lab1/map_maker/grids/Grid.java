package com.example.map_maker.grids;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A Grid that can be moved with [Shift + Left Click] or zoomed using [Mouse wheel]
 */
public class Grid extends Pane {

  /*
    [CONSTRUCTORS]
  */

  public Grid(int _rows, int _cols, int _gridTileSize, double _gridPadding){
    this(_rows, _cols, _gridTileSize, _gridPadding, 5);
  }

  /**
   @param _maxScaleFactor Recommended values (1 - 25)
   */
  public Grid(int _rows, int _cols, int _gridTileSize, double _gridPadding, double _maxScaleFactor) {

    this.maxScaleFactor = _maxScaleFactor / 1000;
    this.rows = _rows;
    this.cols = _cols;

    this.gridTileSize = _gridTileSize;
    this.gridPadding = _gridPadding;

    canvas = new Canvas();
    canvas.widthProperty().bind(this.widthProperty());
    canvas.heightProperty().bind(this.heightProperty());

    canvas.widthProperty().addListener( e -> {
      setMinMaxScale();
      offsetX = _gridPadding;
      offsetY = _gridPadding;
    });
    canvas.heightProperty().addListener( e -> {
      setMinMaxScale();
      offsetX = _gridPadding;
      offsetY = _gridPadding;
    });

    canvas.widthProperty().addListener( e -> draw());
    canvas.heightProperty().addListener( e -> draw());

    getChildren().add(canvas);

    initPanningZoom();
  }

  /*
    [PUBLIC FIELDS]
  */

  /*
    [PUBLIC METHODS]
  */

  /**
   * draws / redraws the grid
   */
  public void draw(){
    drawCanvas();
    drawGrid();
  }

  /**
   * Sets the size of the grid
   */
  public void setRowsCols(int _rows, int _cols){
    this.rows = _rows;
    this.cols = _cols;
    offsetX = gridPadding;
    offsetY = gridPadding;
    setMinMaxScale();
    clampOffset();
    draw();
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  protected int rows;
  protected int cols;
  protected final int gridTileSize;

  protected double scale = 1.0;
  protected double minScale;
  protected double maxScale;

  protected final double maxScaleFactor;
  protected final double gridPadding;
  protected final double outlineSize = 2.0;

  protected double offsetX = 0, offsetY = 0;
  private double dragStartX, dragStartY;
  protected boolean isDragging = false;

  protected final Canvas canvas;

  /*
    [PRIVATE / PROTECTED METHODS]
   */

  protected void drawCanvas(){
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Set BG color for entire canvas
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Canvas border
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(outlineSize);
    gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
  protected void drawGrid() {

    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Canvas border
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(outlineSize);
    gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw tile outlines
    // Horizontal lines
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(outlineSize);
    for (int i = 0; i <= rows; i++) {
      double yPos = offsetY + i * gridTileSize * scale;
      double xStart = offsetX;
      double xEnd = offsetX + cols * gridTileSize * scale;
      gc.strokeLine(
          xStart,
          yPos,
          xEnd,
          yPos
      );
    }
    // Vertical lines
    for (int i = 0; i <= cols; i++) {
      double xPos = offsetX + i * gridTileSize * scale;
      double yStart = offsetY;
      double yEnd = offsetY + rows * gridTileSize * scale;
      gc.strokeLine(
          xPos,
          yStart,
          xPos,
          yEnd
      );
    }

  }

  /**
   * Adds mouse / scroll events to enable panning / zooming
   */
  private void initPanningZoom() {

    // Zooming with mouse wheel
    canvas.addEventFilter(ScrollEvent.SCROLL, e -> {
      // There are scenarios where minScale > maxScale because the grid is too small to zoom inside. In that case we don't try to zoom
      if(minScale > maxScale){
        return;
      }

      //  For some reason scrolling sends 2 events one with 0 delta and the other with actual scroll value
      if(e.getDeltaY() == 0){
        return;
      }

      double oldScale = scale;

      scale += e.getDeltaY() > 0 ? 0.1 : -0.1;

      scale = Math.clamp(scale, minScale, maxScale);

      double mouseX = e.getX();
      double mouseY = e.getY();

      offsetX = mouseX - ((mouseX - offsetX) / oldScale) * scale;
      offsetY = mouseY - ((mouseY - offsetY) / oldScale) * scale;

      clampOffset();
      draw();
      e.consume();
    });
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      if (e.isShiftDown() && e.getButton() == MouseButton.PRIMARY) {
        isDragging = true;
        dragStartX = e.getX() - offsetX;
        dragStartY = e.getY() - offsetY;
      }
    });
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
      isDragging = false;
    });
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
      if (isDragging) {
        offsetX = e.getX() - dragStartX;
        offsetY = e.getY() - dragStartY;
        clampOffset();
        draw();
      }
    });
  }

  /**
   * Clamps the grid to the panel so it can't go outside viewable area
   */
  protected void clampOffset(){

    double maxOffset = gridPadding;

    // Calculate the horizontal gap from grid right to canvas right
    double hGap = -offsetX + canvas.getWidth() - (cols * gridTileSize * scale);
    // Calculate the vertical gap from grid bottom to canvas bottom
    double vGap = -offsetY + canvas.getHeight() - (rows * gridTileSize * scale);

    /*
      Horizontal panning correction
      if - grid width bigger than canvas + padding
      else - grid width smaller than canvas + padding
     */
    if((cols * gridTileSize * scale) > canvas.getWidth() - gridPadding * 2){
      if(hGap > maxOffset){
        offsetX += hGap - maxOffset;
      }
      if(offsetX > maxOffset){
        offsetX = maxOffset;
      }
    }
    else {
      if(hGap < maxOffset){
        offsetX += hGap - maxOffset;
      }
      if(offsetX < maxOffset){
        offsetX = maxOffset;
      }
    }

    /*
      Vertical panning correction
      if - grid height bigger than canvas + padding
      else - grid height smaller than canvas + padding
     */
    if((rows * gridTileSize * scale) > canvas.getHeight() - gridPadding * 2){
      if(vGap > maxOffset){
        offsetY += vGap - maxOffset;
      }
      if(offsetY > maxOffset){
        offsetY = maxOffset;
      }
    }
    else {
      if(vGap < maxOffset){
        offsetY += vGap - maxOffset;
      }
      if(offsetY < maxOffset){
        offsetY = maxOffset;
      }
    }
  }

  /**
   * Sets the minimum and maximum scale of the Grid based on [scaleFactor] and canvas size
   */
  protected void setMinMaxScale(){

    double scaleVertical = (canvas.getHeight() - gridPadding * 2) / (gridTileSize * rows);
    double scaleHorizontal = (canvas.getWidth() - gridPadding * 2) / (gridTileSize * cols);

    minScale = Math.min(scaleVertical, scaleHorizontal);
    scale = minScale;

    maxScale = Math.sqrt(canvas.getHeight() * canvas.getWidth() * maxScaleFactor / (gridTileSize * gridTileSize));
  }

}