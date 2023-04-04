
import org.example.Main;
import org.example.UserController;
import org.junit.jupiter.api.Test;

public class TestCreate {

  @Test
  public void create1() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create1.txt");
    client.stopConnection();

  }

  @Test
  public void create2() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create2.txt");
    client.stopConnection();

  }

  @Test
  public void create3() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create3.txt");
    client.stopConnection();

  }

  @Test
  public void create4() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create4.txt");
    client.stopConnection();

  }

  @Test
  public void create5() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create5.txt");
    client.stopConnection();

  }

  @Test
  public void create6() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create6.txt");
    client.stopConnection();

  }

  @Test
  public void create7() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/create7.txt");
    client.stopConnection();

  }

  @Test
  public void createAll() throws InterruptedException {
    Thread server = new Thread(() -> Main.main(null));
    Thread client1 = new Thread(this::create1);
    Thread client2 = new Thread(this::create2);
    Thread client3 = new Thread(this::create3);
    Thread client4 = new Thread(this::create4);
    Thread client5 = new Thread(this::create5);
    Thread client6 = new Thread(this::create6);
    Thread client7 = new Thread(this::create7);

    server.start();
    client1.start();
    client2.start();
    client3.start();
    client4.start();
    client5.start();
    client6.start();
    client7.start();
  }

  @Test
  public void trans1() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions1.txt");
    client.stopConnection();
  }

  @Test
  public void trans2() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions2.txt");
    client.stopConnection();
  }

  @Test
  public void trans3() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions3.txt");
    client.stopConnection();
  }

  @Test
  public void trans4() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions4.txt");
    client.stopConnection();
  }

  @Test
  public void trans5() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions5.txt");
    client.stopConnection();
  }

  @Test
  public void trans6() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions6.txt");
    client.stopConnection();
  }

  @Test
  public void trans7() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/main/java/org/example/transactions7.txt");
    client.stopConnection();
  }

  @Test
  public void transAll() {
    Main.main(null);
    trans1();
//    trans2();
//    trans3();
//    trans4();
//    trans5();
//    trans6();
  }
}
