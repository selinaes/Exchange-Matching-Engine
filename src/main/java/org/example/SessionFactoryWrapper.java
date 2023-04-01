package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionFactoryWrapper {
//  public static final String DB_DRIVER = "org.postgresql.Driver";
//  public static final String DB_URL = "jdbc:postgresql://localhost:5432/stock_exchange";
//
//  public static final String DB_USER = "postgres";
//  public static final String DB_PASSWORD = "ruqiulixia0220";

  private static SessionFactoryWrapper instance;
  private SessionFactory sessionFactory;

  private SessionFactoryWrapper() {
    try {
//      Class.forName(DB_DRIVER);
//      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//      connection.setAutoCommit(false);

      this.initSessionFactory();

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");
  }


  private void initSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure("hibernate.cfg.xml");
//    System.out.println(configuration.getProperties().toString());
//    configuration.addAnnotatedClass(org.example.models.Account.class);
//    configuration.addAnnotatedClass(org.example.models.Order.class);
//    configuration.addAnnotatedClass(org.example.models.Position.class);
//    StandardServiceRegistryBuilder builder =
//            new org.hibernate.boot.registry.StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    sessionFactory = configuration.buildSessionFactory();

  }

//  public static synchronized Connection getConnectionInstance() {
//    if (instance == null) {
//      instance = new DBConnection();
//    }
//    return instance.connection;
//  }

  public static synchronized SessionFactory getSessionFactoryInstance() {
    if (instance == null) {
      instance = new SessionFactoryWrapper();
    }
    return instance.sessionFactory;
  }

  public static synchronized void shutdown() {
    getSessionFactoryInstance().close();
  }
}
