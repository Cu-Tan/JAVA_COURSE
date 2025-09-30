package com.example.map_maker.grids;

import com.example.map_maker.TileGraphicInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Random;
import java.util.function.Consumer;

public class SampleGrid extends Grid {

  /*
    [CONSTRUCTORS]
  */

  public SampleGrid(int _rows, int _cols, int _cellSize, double _gridPadding){
    this(_rows, _cols, _cellSize, _gridPadding, 5);
  }
  /**
   @param _scaleFactor Recommended values (1 - 25)
   */
  public SampleGrid(int _rows, int _cols, int _cellSize, double _gridPadding, double _scaleFactor) {
    super(_rows, _cols, _cellSize, _gridPadding, _scaleFactor);
    setupEvents();
  }

  /*
    [PUBLIC FIELDS]
  */

  public static int sampleTileSize = 16;

  /*
    [PUBLIC METHODS]
  */

  @Override
  public void draw(){
    drawCanvas();
    drawTiles();
    drawHoverEffect();
    drawGrid();
    drawSelectedEffect();
  }

  /**
   * Loads the desired sample image and make the grid display it
   * @param _sampleImageFileName - just the file name of a desired sample image
   * @return
   */
  public boolean setSampleImage(String _sampleImageFileName){

    Image sampleImage = new Image(_sampleImageFileName);

    if(sampleImage.isError()){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Bad file selected");
      alert.setContentText("The file is not an image (or something else went wrong)");
      alert.showAndWait();
      return false;
    }
    if(sampleImage.getHeight() % sampleTileSize != 0 || sampleImage.getWidth() % sampleTileSize != 0){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Bad file selected");
      alert.setContentText("The file must have width/height as multiples of 16");
      alert.showAndWait();
      return false;
    }

    selectedCords = null;

    sampleImageFileName = _sampleImageFileName.substring(_sampleImageFileName.lastIndexOf('/') + 1);

    this.rows = (int) (sampleImage.getHeight() / sampleTileSize);
    this.cols = (int) (sampleImage.getWidth() / sampleTileSize);

    initTiles();

    // Initialize tile images to that of sampleImage tiles 16x16
    PixelReader pr = sampleImage.getPixelReader();
    for(int y = 0; y < rows; ++y){
      for(int x = 0; x < cols; ++x){
        tiles[y][x].setImage( new WritableImage(
            pr,
            x * sampleTileSize,
            y * sampleTileSize,
            sampleTileSize,
            sampleTileSize
        ));
      }
    }
    super.setMinMaxScale();
    draw();
    return true;
  }
  public void setOnTileClick(Consumer<TileGraphicInfo> _onTileClick){
    onTileClick = _onTileClick;
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private String sampleImageFileName;
  private Consumer<TileGraphicInfo> onTileClick;

  private Tile[][] tiles = new Tile[0][0];
  private Pair<Integer, Integer> hoverCords;
  private Pair<Integer, Integer> selectedCords;

  private static class Tile{

    static Image generateImage(){
      WritableImage img = new WritableImage(16,16);
      PixelWriter pw = img.getPixelWriter();
      Random rand = new Random();
      int color = 0xFF000000 | rand.nextInt(0xFFFFFF);

      for(int y = 0; y < 16; ++y){
        for(int x = 0; x < 16; ++x){
          pw.setArgb(x, y, color);
        }
      }

      return img;
    }

    public Image getImage(){
      return this.image;
    }
    public void setImage(Image _image){
      this.image = _image;
    }

    private Image image;
  }

  /*
    [PRIVATE / PROTECTED METHODS]
  */

  /**
   * Adds mouse events to pick image and update mapPainter via callback
   */
  private void setupEvents(){
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      if(!isDragging && hoverCords != null){

        selectedCords = hoverCords;

        int tileNumber = selectedCords.getKey() * cols + selectedCords.getValue();

        TileGraphicInfo tgi = new TileGraphicInfo(tileNumber, sampleImageFileName, tiles[selectedCords.getKey()][selectedCords.getValue()].getImage());
        onTileClick.accept(tgi);

        draw();
      }
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
  private void drawTiles(){
    GraphicsContext gc = canvas.getGraphicsContext2D();

    for(int y = 0; y < rows; ++y){
      for(int x = 0; x < cols; ++x){
        double xPos = offsetX + x * gridTileSize * scale;
        double yPos = offsetY + y * gridTileSize * scale;
        if(tiles[y][x].getImage() != null) {
          gc.drawImage(tiles[y][x].getImage(), xPos, yPos, gridTileSize * scale, gridTileSize * scale);
        }
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
  private void drawSelectedEffect(){
    GraphicsContext gc = canvas.getGraphicsContext2D();
    if(selectedCords != null){
      double realTileSize = gridTileSize * scale;
      double topLeftX = offsetX + selectedCords.getValue() * realTileSize;
      double topLeftY = offsetY + selectedCords.getKey() * realTileSize;

      gc.setStroke(Color.RED);
      gc.setLineWidth(outlineSize);
      gc.strokeRect(topLeftX, topLeftY, realTileSize, realTileSize);
    }
  }

  private void initTiles() {
    tiles = new Tile[rows][cols];
    for (int y = 0; y < rows; ++y) {
      for (int x = 0; x < cols; ++x) {
        tiles[y][x] = new Tile();
      }
    }
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
