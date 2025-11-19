package com.example.server.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SocketHelper {

  public static void bufferUUID(OutputStream _oStream, UUID _uuid) throws IOException {

    long msb = _uuid.getMostSignificantBits();
    long lsb = _uuid.getLeastSignificantBits();

    // Write MSB
    for(int i = 7; i >= 0; --i){
      _oStream.write( (int)(msb >> (i * 8)) );
    }

    // Write LSB
    for(int i = 7; i >= 0; --i){
      _oStream.write( (int)(lsb >> (i * 8)) );
    }

  }
  public static UUID readUUID(InputStream _iStream) throws IOException, DisconnectException {

    long msb = 0;
    long lsb = 0;

    // Read MSB
    for(int i = 0; i < 8; ++i){

      int iByte = _iStream.read();
      if(iByte == -1){
        throw new DisconnectException();
      }

      msb = (msb << 8) | iByte;
    }

    // read LSB
    for(int i = 0; i < 8; ++i){

      int iByte = _iStream.read();
      if(iByte == -1){
        throw new DisconnectException();
      }

      lsb = (lsb << 8) | iByte;

    }

    return new UUID(msb, lsb);

  }

  public static void bufferString(OutputStream _oStream, String _string, int _byteLength) throws IOException {

    bufferLength(_oStream, _string.length(), _byteLength);
    _oStream.write(_string.getBytes());

  }
  public static String readString(InputStream _iStream, int _byteLength) throws IOException, DisconnectException {

    int stringLength = readLength(_iStream, _byteLength);

    byte[] stringBytes = new byte[stringLength];
    int stringBytesRead = _iStream.readNBytes(stringBytes, 0, stringLength);
    if(stringBytesRead != stringLength){
      throw new DisconnectException();
    }

    return new String(stringBytes, StandardCharsets.UTF_8);

  }

  public static void bufferLength(OutputStream _oStream, int _value, int _byteLength) throws IOException {

    if(_byteLength < 1 || _byteLength > 4){
      throw new IllegalArgumentException("Invalid byteLength");
    }

    int shiftValue = 8 * (_byteLength - 1);
    for(int i = 0; i < _byteLength; ++i){

      _oStream.write(_value >> shiftValue);
      shiftValue -= 8;

    }

  }
  public static int readLength(InputStream _iStream, int _byteLength) throws IOException, DisconnectException {

    // Realized there are no compile time checks for parameter values. How sad is that :(
    if(_byteLength < 1 || _byteLength > 4){
      throw new IllegalArgumentException("Invalid byteLength");
    }

    int length = 0;

    int shiftValue = 8 * (_byteLength - 1);
    for(int i = 0; i < _byteLength; ++i){
      int iByte = _iStream.read();
      if(iByte == -1){
        throw new DisconnectException();
      }
      length |= iByte << shiftValue;
      shiftValue -= 8;
    }

    return length;

  }

}
