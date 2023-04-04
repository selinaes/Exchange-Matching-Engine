package org.example;

import org.example.models.Account;
import org.hibernate.Session;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      Server server = new Server(12345);
//      Session session = SessionFactoryWrapper.openSession();
//      Account account = new Account();
//      session.beginTransaction();
//      account.setId("1234");
//      account.setBalance(1000.0);
//      session.persist(account);
//      session.getTransaction().commit();
//      session.close();
//      Session session1 = SessionFactoryWrapper.openSession();
//      Account account1 = new Account();
//      session.beginTransaction();
//      account1.setId("123");
//      account1.setBalance(1000.0);
//      session.persist(account1);
//      session.getTransaction().commit();
//      session.close();
//      System.out.println("saved account");
      server.run();
      SessionFactoryWrapper.shutdown();
    } catch (IOException e) {
      System.out.println("Error starting server: " + e.getMessage());
    }
  }
}