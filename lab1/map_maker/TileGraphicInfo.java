package com.example.map_maker;

import javafx.scene.image.Image;

public class TileGraphicInfo {

  /*
    [CONSTRUCTORS]
  */

  /**
   *
   * @param _tileNumber - number of a sub image inside a tile set counting starting from top left / left to right / top to bottom
   * @param _fileName - file from where the sub image originates from
   * @param _img - the sub image from a tile set
   */
  public TileGraphicInfo(int _tileNumber, String _fileName, Image _img){
    this.tileNumber = _tileNumber;
    this.fileName = _fileName;
    this.img = _img;
  }

  /*
    [PUBLIC FIELDS]
  */
  /*
    [PUBLIC METHODS]
  */

  public void setTileGraphicInfo(int _tileNumber, String _fileName, Image _img){
    this.tileNumber = _tileNumber;
    this.fileName = _fileName;
    this.img = _img;
  }
  public int getTileNumber(){
    return tileNumber;
  }
  public String getFileName(){
    return fileName;
  }
  public Image getImage(){
    return img;
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private int tileNumber;
  private String fileName;
  private Image img;

  /*
    [PRIVATE / PROTECTED METHODS]
  */
}
