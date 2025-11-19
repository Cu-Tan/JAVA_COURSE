package com.example.server;

import com.example.server.logic.data.DataRepository;
import com.example.server.ui.Root;
import javafx.application.Application;

/**
 * @author Karolis Ribačonka Informatika 2 kursas 2 grupė 2 pogrupis
 */
public class Main {

  public static void main(String[] args) {

    DataRepository.createInstance();

    Application.launch(Root.class, args);

  }

}
