package com.example.student_registry.logic;

import java.util.ArrayList;

public class UIUpdater {

  // region Singleton

  public static UIUpdater getInstance(){
    if(instance == null){
      instance = new UIUpdater();
    }
    return instance;
  }
  private static UIUpdater instance;
  private UIUpdater(){}

  // endregion

  public void subscribe(UIUpdatable _ui){
    uis.add(_ui);
  }
  public void unsubscribe(UIUpdatable _ui){
    uis.remove(_ui);
  }
  public void update(UIUpdateType _updateType){
    for(UIUpdatable ui: uis){
      ui.update(_updateType);
    }
  }

  private ArrayList<UIUpdatable> uis = new ArrayList<>();

}
