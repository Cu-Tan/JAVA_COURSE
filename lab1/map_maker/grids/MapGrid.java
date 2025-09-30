package com.example.map_maker.grids;

import com.example.map_maker.types.EntityTypes;
import com.example.map_maker.map.Map;
import com.example.map_maker.MapPainter;
import com.example.map_maker.types.TileTypes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class MapGrid extends Grid {

  /*
    [CONSTRUCTORS]
  */

  public MapGrid(int _rows, int _cols, int _defaultGridTileSize, double _gridPadding, MapPainter _mapPainter){
    this(_rows, _cols, _defaultGridTileSize, _gridPadding, 5, _mapPainter);
  }
  /**
   @param _scaleFactor Recommended values (1 - 25)
   */
  public MapGrid(int _rows, int _cols, int _defaultGridTileSize, double _gridPadding, double _scaleFactor, MapPainter _mapPainter) {
    super(_rows, _cols, _defaultGridTileSize, _gridPadding, _scaleFactor);
    map = new Map(_rows, _cols);
    mapPainter = _mapPainter;
    setupEvents();
  }

  /*
    [PUBLIC FIELDS]
  */

  public boolean showTileTypeHint = false;
  public boolean showEntitiesHint = false;

  public MapPainter mapPainter;

  /*
    [PUBLIC METHODS]
  */

  /**
   * draws / redraws the grid
   */
  @Override
  public void draw(){
    drawCanvas();
    drawTiles();
    drawHoverEffect();
    drawGrid();
    drawTileTypeHints();
    drawEntitiesHints();
  }

  /**
   * Sets the size of the grid and current [Map]
   */
  @Override
  public void setRowsCols(int _rows, int _cols){

    setRowsColsGrid(_rows, _cols);

    Map newMap = new Map(_cols, _rows);
    newMap.copyTiles(map);
    map = newMap;

    draw();
  }

  /**
   * Sets the grid to display a given map
   */
  public void setMap(Map _map){
    this.map = _map;
    setRowsColsGrid(map.height, map.width);
    draw();
  }

  public Map getMap(){
    return this.map;
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private Map map;

  private Pair<Integer, Integer> hoverCords;
  private boolean isDrawing = false;

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  /**
   * Sets the size of the GRID
   */
  private void setRowsColsGrid(int _rows, int _cols){
    this.rows = _rows;
    this.cols = _cols;
    offsetX = gridPadding;
    offsetY = gridPadding;

    setMinMaxScale();
    clampOffset();
  }

  private void drawTiles(){
    GraphicsContext gc = canvas.getGraphicsContext2D();

    for(int y = 0; y < rows; ++y){
      for(int x = 0; x < cols; ++x){

        if(map.tiles[y][x].getImage() == null){
          continue;
        }

        double xPos = offsetX + x * gridTileSize * scale;
        double yPos = offsetY + y * gridTileSize * scale;
        gc.drawImage(map.tiles[y][x].getImage(), xPos, yPos, gridTileSize * scale, gridTileSize * scale);
      }
    }
  }
  private void drawHoverEffect(){
    GraphicsContext gc = canvas.getGraphicsContext2D();
    if(hoverCords != null){
      gc.setGlobalAlpha(0.2);
      gc.setFill(Color.BLACK);
      double tileSize = this.gridTileSize * scale;
      gc.fillRect(offsetX + hoverCords.getValue() * tileSize, offsetY + hoverCords.getKey() * tileSize, tileSize, tileSize);
      gc.setGlobalAlpha(1);
    }
  }
  private void drawTileTypeHints(){

    if(!showTileTypeHint){
      return;
    }

    GraphicsContext gc = canvas.getGraphicsContext2D();

    for(int y = 0; y < rows; ++y){
      for(int x = 0; x < cols; ++x) {

        if(map.tiles[y][x].tileType == TileTypes.NONE){
          continue;
        }

        double xPos = offsetX + x * gridTileSize * scale;
        double yPos = offsetY + y * gridTileSize * scale;

        gc.setGlobalAlpha(0.5);
        gc.setFill(map.tiles[y][x].tileType.color);
        gc.fillRect(xPos, yPos, gridTileSize * scale, gridTileSize * scale);
        gc.setGlobalAlpha(1);

      }
    }
  }
  private void drawEntitiesHints(){

    if(!showEntitiesHint){
      return;
    }

    GraphicsContext gc = canvas.getGraphicsContext2D();

    for(int y = 0; y < rows; ++y){
      for(int x = 0; x < cols; ++x) {

        if (map.tiles[y][x].entityType == EntityTypes.NONE){
          continue;
        }

        double xPos = offsetX + x * gridTileSize * scale;
        double yPos = offsetY + y * gridTileSize * scale;
        gc.setStroke(map.tiles[y][x].entityType.color);
        gc.setFill(map.tiles[y][x].entityType.color);

        switch (map.tiles[y][x].entityType){
          case PLAYER -> {
            gc.fillText("P", xPos + gridTileSize * scale / 2, yPos + gridTileSize * scale / 2);
          }
          case ENEMY -> {
            gc.fillText("E", xPos + gridTileSize * scale / 2, yPos + gridTileSize * scale / 2);
          }
          case NEXT_LEVEL_TRIGGER -> {
            gc.fillText("L", xPos + gridTileSize * scale / 2, yPos + gridTileSize * scale / 2);
          }
        }
        gc.strokeRect(xPos, yPos, gridTileSize * scale, gridTileSize * scale);

      }
    }

  }

  /**
   * Adds mouse events that paint on the grid and update the map state
   */
  private void setupEvents(){
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      if(!isDragging && e.getButton() == MouseButton.PRIMARY && hoverCords != null){
        isDrawing = true;
        mapPainter.paint(map.tiles[hoverCords.getKey()][hoverCords.getValue()]);
        draw();
      }
    });
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
      if(!isDragging && isDrawing){
        Pair<Integer, Integer> tileCords = getTileCordsFromMousePos(e.getX(), e.getY());
        if(hoverCords == null && tileCords == null){
          return;
        }
        else if(hoverCords != null && tileCords == null){
          hoverCords = tileCords;
          draw();
        }
        else if(hoverCords == null && tileCords != null){
          hoverCords = tileCords;
          mapPainter.paint(map.tiles[hoverCords.getKey()][hoverCords.getValue()]);
          draw();
        }
        else if(hoverCords != null && tileCords != null && !hoverCords.equals(tileCords)){
          hoverCords = tileCords;
          mapPainter.paint(map.tiles[hoverCords.getKey()][hoverCords.getValue()]);
          draw();
        }
      }
    });
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
      isDrawing = false;
    });
    canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      if(!isDragging){
        Pair<Integer, Integer> tileCords = getTileCordsFromMousePos(e.getX(), e.getY());
        if(hoverCords == null && tileCords == null){
          return;
        }
        else if(hoverCords != null && tileCords != null && !hoverCords.equals(tileCords)){
          hoverCords = tileCords;
          draw();
        }
        else {
          hoverCords = tileCords;
          draw();
        }
      }
    });
  }
  private Pair<Integer, Integer> getTileCordsFromMousePos(double mouseX, double mouseY){

    if(
        (mouseX > offsetX && mouseX < offsetX + cols * gridTileSize * scale)
            &&
            (mouseY > offsetY && mouseY < offsetY + rows * gridTileSize * scale)
    ) {
      mouseX -= offsetX;
      mouseY -= offsetY;

      int col = (int) (mouseX / (gridTileSize * scale));
      int row = (int) (mouseY / (gridTileSize * scale));

      return new Pair<Integer, Integer>(row, col);

    }
    return null;
  }
}