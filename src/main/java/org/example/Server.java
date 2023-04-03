package org.example;

import org.example.requests.Request;
import org.example.results.Result;
import org.hibernate.Session;
import org.xml.sax.SAXException;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;


public class Server {
  private final ServerSocket serverSocket;
  BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
  ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.MILLISECONDS, workQueue);
  ArrayList<Client> clients = new ArrayList<>();


  public Server(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);

  }

  public void run() {
    System.out.println("Server started");
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
    String length;
    String xml;
    try {
      length = client.recvLine();
      int xmlLen = Integer.parseInt(length);
//      System.out.println("xmlLen is " + xmlLen);
      xml = client.recvBytes(xmlLen);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      System.out.println("Received xml: ");
      System.out.println(xml);
      Request r = Parser.parseXML(xml);
      Result response = r.execute();
//      System.out.println(r);
      client.send(response.toXMLString());

//      System.out.println(response.toXMLString());
    } catch (ParserConfigurationException e) {
      System.out.println("ParserConfigurationException");
    } catch (IOException e) {
      System.out.println("IOException");
    } catch (SAXException e) {
      System.out.println("SAXException");
    }


//    if (xml == null) {
//      System.out.println("xml not received");
//    } else {
//      System.out.println("Received xml: ");
//      System.out.println(xml);
//      Request r = Parser.parseXML(xml);
////      Session session = SessionFactoryWrapper.getSessionFactoryInstance().openSession();
//      r.execute();
//      System.out.println(r);
//      // create request, process, send response
//    }
  }

  /**
   * This is a helper method to accept a socket from the ServerSocket or return null if it
   * timeouts.
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
