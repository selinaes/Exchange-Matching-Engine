
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
  public void createAll() {
    Main.main(null);
    create1();
    create2();
    create3();
    create4();
    create5();
  }
}
