package org.example;


import java.io.IOException;

public class Main {
  public static int PORT = 12345;

  public static void main(String[] args) {
    try {
      Server server = new Server(PORT);
      server.run();
    } catch (IOException e) {
      System.out.println("Error starting server: " + e.getMessage());
    } finally {
      SessionFactoryWrapper.shutdown();
    }
  }
}