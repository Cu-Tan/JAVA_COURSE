package com.example.game.entities;

import com.example.game.helper.Coordinates;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity {

  /*
    [CONSTRUCTORS]
  */

  public Entity(Coordinates _coordinates, Image _sprite, double _size){
    this.coordinates = _coordinates;
    this.sprite = new ImageView(_sprite);
    this.sprite.setFitHeight(_size);
    this.sprite.setFitWidth(_size);
    this.size = _size;
  }

  /*
    [PUBLIC FIELDS]
  */

  public Coordinates coordinates;
  public ImageView sprite;
  public double size;

  /*
    [PUBLIC METHODS]
  */

  void update(){}

  public void render(Coordinates camOffset){
    sprite.setX(coordinates.x + camOffset.x - size / 2);
    sprite.setY(coordinates.y + camOffset.y - size / 2);
  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */
  /*
    [PRIVATE / PROTECTED METHODS]
  */
}
