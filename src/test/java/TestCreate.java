
import org.example.Main;
import org.example.UserController;
import org.junit.jupiter.api.Test;

public class TestCreate {

  @Test
  public void create1() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create1.txt");
    client.stopConnection();

  }

  @Test
  public void create2() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create2.txt");
    client.stopConnection();

  }

  @Test
  public void create3() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create3.txt");
    client.stopConnection();

  }

  @Test
  public void create4() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create4.txt");
    client.stopConnection();

  }

  @Test
  public void create5() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create5.txt");
    client.stopConnection();

  }

  @Test
  public void create6() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create6.txt");
    client.stopConnection();

  }

  @Test
  public void create7() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/create7.txt");
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

    Thread.sleep(1000);
    client1.start();
    Thread.sleep(1000);
    client2.start();
    Thread.sleep(1000);
    client3.start();
    Thread.sleep(1000);
    client4.start();
    Thread.sleep(1000);
    client5.start();
    Thread.sleep(1000);
    client6.start();
    Thread.sleep(1000);
    client7.start();

    server.join();
    client1.join();
    client2.join();
    client3.join();
    client4.join();
    client5.join();
    client6.join();
    client7.join();
  }

  @Test
  public void trans1() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions1.txt");
    client.stopConnection();
  }

  @Test
  public void trans2() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions2.txt");
    client.stopConnection();
  }

  @Test
  public void trans3() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions3.txt");
    client.stopConnection();
  }

  @Test
  public void trans4() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions4.txt");
    client.stopConnection();
  }

  @Test
  public void trans5() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions5.txt");
    client.stopConnection();
  }

  @Test
  public void trans6() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions6.txt");
    client.stopConnection();
  }

  @Test
  public void trans7() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/transactions7.txt");
    client.stopConnection();
  }

  @Test
  public void transAll() throws InterruptedException {
//    Thread server = new Thread(() -> Main.main(null));
    Thread client1 = new Thread(this::trans1);
    Thread client2 = new Thread(this::trans2);
    Thread client3 = new Thread(this::trans3);
    Thread client4 = new Thread(this::trans4);
    Thread client5 = new Thread(this::trans5);
    Thread client6 = new Thread(this::trans6);
    Thread client7 = new Thread(this::trans7);


//    server.start();
    client1.start();
    client2.start();
    client3.start();
    client4.start();
    client5.start();
    client6.start();
    client7.start();

//    server.join();
    client1.join();
    client2.join();
    client3.join();
    client4.join();
    client5.join();
    client6.join();
    client7.join();
  }

  @Test
  public void query7() {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    client.sendFile("src/test/java/query7.txt");
    client.stopConnection();
  }
}
