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
      System.out.println("Sending file");
      while ((line = reader.readLine()) != null) {
        out.println(line);
        System.out.println(line);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String msg) {
    out.println(msg);

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

  public String recvMsg() {
    StringBuilder sb = new StringBuilder();
    try {
      String line = this.in.readLine();
      while (line != null) {
        sb.append(line);
        if (line.endsWith("</results>")) {
          break; // exit the loop if we have received the complete message
        }
        line = in.readLine();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);

//    client.sendFile("src/main/java/org/example/create7.txt");
//    client.sendFile("src/main/java/org/example/transactions7.txt");
    client.sendFile("src/main/java/org/example/query7.txt");

    client.stopConnection();
  }
}
