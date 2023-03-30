package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class T1 {
  public static void main(String[] args) {
    try {
      Socket socket = new Socket("localhost", 12345);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      File inputFile = new File("src/main/java/org/example/input.txt");
      BufferedReader reader = new BufferedReader(new FileReader("src/main/java/org/example/input.txt"));
//      StringBuilder stringBuilder = new StringBuilder();
      String line = null;
      String ls = System.getProperty("line.separator");
      out.write("300");
      out.write(ls);
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
//        stringBuilder.append(line);
//        stringBuilder.append(ls);
        out.write(line);
        out.write(ls);
      }
// delete the last new line separator
//      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      reader.close();

//      String content = stringBuilder.toString();
//      System.out.println(content);


      out.close();
      socket.close();
      in.close();


    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
