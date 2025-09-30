package com.example.game;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class TempImage {

  public static Image generateRandomImage(int _size){
    WritableImage img = new WritableImage(_size, _size);
    PixelWriter pw = img.getPixelWriter();
    Random rand = new Random();
    Color randomColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    for(int y = 0; y < _size; ++y){
      for(int x = 0; x < _size; ++x){
        pw.setColor(y, x, randomColor);
      }
    }
    return img;
  }
}
