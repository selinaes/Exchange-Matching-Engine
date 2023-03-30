package org.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Server {
  private ServerSocket serverSocket;
  BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
  ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.MILLISECONDS, workQueue);
  ArrayList<Client> clients = new ArrayList<>();

  Controller controller = new Controller();

  public Server(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);

  }

  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      final Socket client_socket = acceptOrNull();
      Client client = new Client(client_socket);
      clients.add(client);
      if (client_socket == null) {
        continue;
      }
      executor.execute(new Runnable() {
        @Override
        public void run() {
          try {
            // do something with client_socket
            System.out.println("Client accepted");
            handleClient(client);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }

  public void handleClient(Client client) {
    String length = null;
    String xml = null;
    try {
      length = client.recvLine();
      int xmlLen = Integer.parseInt(length);
      System.out.println("xmlLen is " + xmlLen);
      xml = client.recvBytes(xmlLen);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (xml == null){
        System.out.println("xml not received");
      } else {
      System.out.println("Received xml: ");
        System.out.println(xml);
        controller.parseXML(xml);
        // create request, process, send response
      }
  }

  /**
   * This is a helper method to accept a socket from the ServerSocket or return null if it
   * timesout.
   *
   * @return the socket accepted from the ServerSocket
   */
  public Socket acceptOrNull() {
    try {
      return serverSocket.accept();
    } catch (IOException ioe) {
      return null;
    }
  }
}
