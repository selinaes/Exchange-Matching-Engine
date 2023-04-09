package org.example;

import org.example.requests.Request;
import org.example.results.Result;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.SubResult;
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
  BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(100);
  ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 5, TimeUnit.MILLISECONDS, workQueue);
//  ArrayList<Client> clients = new ArrayList<>();
  int numClients;


  public Server(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);
    this.numClients = 0;

  }

  public void run() {
    System.out.println("Server started");
    while (!Thread.currentThread().isInterrupted()) {
      final Socket client_socket = acceptOrNull();
      Client client = new Client(client_socket);
//      clients.add(client);
      synchronized (this){
        numClients++;
      }
      if (client_socket == null) {
        continue;
      }
      executor.execute(new Runnable() {
        @Override
        public void run() {
          try {
            // do something with client_socket
            synchronized (this) {
              System.out.println("Client accepted" + numClients);
            }
            handleClient(client);
          } catch (Exception e) {
            e.printStackTrace();
          }
          finally {
            try {
              client_socket.close();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
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
      System.out.println("xmlLen is " + xmlLen);
      xml = client.recvBytes(xmlLen);
//      xml = client.recv();

      System.out.println("Received xml: ");
      System.out.println(xml);
      Request r = Parser.parseXML(xml);
      Result response = r.execute();

      client.send(response.toXMLString());
    } catch (IOException e) {
      Result result = new Result();
      SubResult error = new ErrorResult(e.getMessage());
      result.addSubResult(error);
      client.send(result.toXMLString());
    }
     catch (ParserConfigurationException e) {
      Result result = new Result();
      SubResult error = new ErrorResult("ParserConfigurationException");
      result.addSubResult(error);
      client.send(result.toXMLString());
    } catch (SAXException e) {
      Result result = new Result();
      SubResult error = new ErrorResult("Given XML length too short, or invalid XML");
      result.addSubResult(error);
      client.send(result.toXMLString());
//      e.printStackTrace();
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
