package com.example.lab6.logic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class CSVReaderRunnable implements Runnable {

  /**
   * @param _file file that the CSVReader should read from
   */
  public CSVReaderRunnable(
    File _file,
    Consumer<Data[]> _sendBatch,
    Consumer<Double> _progressUpdate
  ) {
    file = _file;
    sendBatch = _sendBatch;
    progressUpdate = _progressUpdate;
  }

  @Override
  public void run() {

    try{
      readAll();
    }
    catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

  private final File file;
  private final Consumer<Data[]> sendBatch;
  private final Consumer<Double> progressUpdate;
  private final int MAX_BATCH_LENGTH = 25;

  private void readAll() throws IOException, InterruptedException {

    long fileSize = file.length();
    long bytesRead = 0;

    BufferedReader bufferedReader = new BufferedReader(new FileReader(file) );

    // Read header data
    String line = bufferedReader.readLine();
    bytesRead += (line + "\n").getBytes(StandardCharsets.UTF_8).length;

    ArrayList<Data> batches = new ArrayList<>();

    while( (line = bufferedReader.readLine()) != null){

      bytesRead += (line + "\n").getBytes(StandardCharsets.UTF_8).length;

      Data data = lineToData(line);

      batches.add(data);

      if(batches.size() == MAX_BATCH_LENGTH){
        sendBatch.accept(batches.toArray( new Data[0] ));
        batches.clear();
      }

      progressUpdate.accept( (double) bytesRead / fileSize );

      Thread.sleep(10);

    }

    // if we have data elements but not reached the MAX_BATCH_LENGTH inside while loop above
    if(!batches.isEmpty()){
      sendBatch.accept(batches.toArray( new Data[0] ));
    }

  }

  /**
   * Converts a given csv string to a data object <br>
   * WARNING: No validation. Includes hardcoded values
   * @return data object constructed from a given csv string
   */
  private Data lineToData(
    String _line
  ) {

    String[] dataObjects = _line.split(",");

    int id = Integer.parseInt( dataObjects[0] );
    String
      firstName = dataObjects[1],
      lastName = dataObjects[2],
      email = dataObjects[3],
      gender = dataObjects[4],
      country = dataObjects[5],
      domainName = dataObjects[6],
      dateOfBirth = dataObjects[7]
    ;


    return new Data(
      id, firstName, lastName, email, gender, country, domainName, dateOfBirth
    );

  }

}
