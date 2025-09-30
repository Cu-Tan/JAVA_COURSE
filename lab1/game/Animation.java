package com.example.game;

import com.example.game.game.Time;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Class used for entity animations (One time / looped)
 */
public class Animation {

  /*
    [CONSTRUCTORS]
  */

  public Animation(
      Image[] _frames,
      ImageView _target,
      double _time,
      boolean _loop
  ) {
    this.frames = _frames;
    this.target = _target;
    this.time = _time;
    this.loop = _loop;

    this.currentFrame = 0;
    this.currentTime = 0;

    this.interval = this.time / this.frames.length;

    finished = false;
  }

  /*
    [PUBLIC FIELDS]
  */
  /*
    [PUBLIC METHODS]
  */

  /**
   * Fully reset animation from beginning
   */
  public void reset(){
    currentFrame = 0;
    currentTime = 0;
    target.setImage(frames[currentFrame]);
    finished = false;
  }

  /**
   * Updates the animation frame if needed and updates target image
   */
  public void update(){

    if(finished){
      return;
    }

    currentTime += Time.deltaTime;

    if(currentTime > interval){
      currentTime = 0;
      currentFrame++;

      // If we are not looping and the animation is done set finished flag
      if(!loop && currentFrame == frames.length){
        finished = true;
        return;
      }

      currentFrame %= frames.length;
      target.setImage(frames[currentFrame]);
    }

  }

  /*
    [PRIVATE / PROTECTED FIELDS]
  */

  private Image[] frames;
  private ImageView target;

  private double time;
  private boolean loop;

  private int currentFrame;
  private double currentTime;
  private double interval;

  private boolean finished;

  /*
    [PRIVATE / PROTECTED METHODS]
  */

}
