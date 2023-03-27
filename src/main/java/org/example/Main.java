package org.example;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      Server server = new Server(12345);
      server.run();
    } catch (IOException e) {
      System.out.println("Error starting server: " + e.getMessage());
    }
  }
}