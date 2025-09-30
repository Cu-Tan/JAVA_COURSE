package com.example.game.game;

import com.example.game.entities.Enemy;
import com.example.game.entities.Entity;
import com.example.game.entities.Player;
import com.example.game.entities.Tile;

import java.util.ArrayList;

public class MapData {

  public ArrayList<Tile> bgTiles;
  public ArrayList<Tile> tiles;
  public ArrayList<Enemy> enemies;

  public Player player;

  public ArrayList<Entity> nextLevelTriggers;


  MapData(
      ArrayList<Tile> _bgTiles,
      ArrayList<Tile> _tiles,
      ArrayList<Enemy> _enemies ,
      Player _player,
      ArrayList<Entity> _nextLevelTriggers
  ){
    this.bgTiles = _bgTiles;
    this.tiles = _tiles;
    this.enemies = _enemies;

    this.player = _player;

    this.nextLevelTriggers = _nextLevelTriggers;
  }

}
