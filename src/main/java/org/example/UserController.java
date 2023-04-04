package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserController {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public void startConnection(String ip, int port) {
    try {
      clientSocket = new Socket(ip, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendFile(String path) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(path));
      String line;
      String ls = System.getProperty("line.separator");
//      out.write("173");
//      out.write(ls);
      System.out.println("Sending file");
      while ((line = reader.readLine()) != null) {
//        System.out.println(line);
//        stringBuilder.append(line);
//        stringBuilder.append(ls);
        out.println(line);
//        out.println(ls);
//        out.write(line);
//        out.write(ls);
//        System.out.println(out);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String msg) {
    out.println(msg);
//      String resp = in.readLine();
    out.write(msg);
//      return resp;
    //    return "null";
  }

  public void stopConnection() {
    try {
      in.close();
      out.close();
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void recMsg() {
    try {
//      String input = in.readLine();
      String line = in.readLine();
      while (line != null) {
        System.out.println(line);
        line = in.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
//    System.out.println("Connected");
//    client.sendMessage("173");
//    System.out.println("Connected");
    client.sendFile("src/main/java/org/example/input2.txt");
//    System.out.println(response);
//    client.recMsg();


    client.stopConnection();
  }
}
