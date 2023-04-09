import org.example.UserController;
import org.example.requests.sub_transaction_requests.CancelRequest;
import org.example.requests.sub_transaction_requests.OrderRequest;
import org.example.requests.sub_transaction_requests.QueryRequest;
import org.example.requests.sub_transaction_requests.SubTransactionRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadTest {

//  public static final int MaxBytes = 10000;

  // map is <AccountId, Balance> <symbol, <accountId, count>>
  private void sendCreateRequest(Map<String, Double> create, Map<String, Map<String, Double>> positions) {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    sb.append("<create>");
    for (Map.Entry<String, Double> entry : create.entrySet()) {
      String accountId = entry.getKey();
      Double balance = entry.getValue();
      sb.append(String.format("<account id=\"%s\" balance=\"%s\"/>\n", accountId, balance));
    }

    for (Map.Entry<String, Map<String, Double>> entry : positions.entrySet()) {

      String symbol = entry.getKey();
      sb.append(String.format("<symbol sym=\"%s\">", symbol));
      Map<String, Double> position = entry.getValue();
      for (Map.Entry<String, Double> entry2 : position.entrySet()) {
        String accountId2 = entry2.getKey();
        Double count = entry2.getValue();
        sb.append(String.format("<account id=\"%s\"> %s </account>\n", accountId2, count));
      }
      sb.append("</symbol>\n");
    }
    sb.append("</create>\n");
    final String x = sb.toString().length() + "\n" + sb;
    System.out.println(x);
    client.sendMessage(x);
//    client.sendFile("src/test/java/" + filename);
    client.stopConnection();
  }

  public void sendTransaction(String accountId, List<SubTransactionRequest> subTransactionRequests) {
    UserController client = new UserController();
    client.startConnection("localhost", 12345);
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    sb.append(String.format("<transactions id=\"%s\">\n", accountId));
    for (SubTransactionRequest subTransactionRequest : subTransactionRequests) {
      if (subTransactionRequest instanceof CancelRequest) {
        CancelRequest cancelRequest = (CancelRequest) subTransactionRequest;
        sb.append(String.format("<cancel id=\"%s\"/>\n", cancelRequest.getOrderId()));
      } else if (subTransactionRequest instanceof OrderRequest) {
        OrderRequest orderRequest = (OrderRequest) subTransactionRequest;
        sb.append(String.format("<order sym=\"%s\"  amount=\"%s\" limit=\"%s\"/>\n", orderRequest.getSymbol(), orderRequest.getAmount(), orderRequest.getLimit()));
      } else if (subTransactionRequest instanceof QueryRequest) {
        QueryRequest queryRequest = (QueryRequest) subTransactionRequest;
        sb.append(String.format("<query id=\"%s\"/>\n", queryRequest.getTransactionId()));
      }
    }
    sb.append("</transactions>\n");
    final String x = sb.toString().length() + "\n" + sb;
    System.out.println(x);
    client.sendMessage(x);
    client.stopConnection();
//    System.out.println(sb.toString());
//    client.sendMessage(sb.toString());
  }


  @Test
  public void testLoad() {
    int numRounds = 1;
    int numRequests = 0;
    UserController client = new UserController();
    List<Long> latencies = new ArrayList<>();

    List<List<SubTransactionRequest>> subTrans = testMultiTrans();

    client.startConnection("localhost", 12345);
    long startTime = System.nanoTime();
    long requestStartTime;
    long requestEndTime;
    long latencyMs;
    for (int i = 0; i < numRounds; i++) {
      requestStartTime = System.nanoTime();
      testCreate();
      numRequests++;
      client.recvMsg();
      requestEndTime = System.nanoTime();
      latencyMs = (requestEndTime - requestStartTime) / 1000000;
      latencies.add(latencyMs);

      requestStartTime = System.nanoTime();
      testTrans();
      numRequests++;
      client.recvMsg();
      requestEndTime = System.nanoTime();
      latencyMs = (requestEndTime - requestStartTime) / 1000000;
      latencies.add(latencyMs);

      for (int j = 1; j < 11; j++) {
          requestStartTime = System.nanoTime();
          sendTransaction(String.valueOf(j), subTrans.get(j-1));
          numRequests++;
        client.recvMsg();
        requestEndTime = System.nanoTime();
        latencyMs = (requestEndTime - requestStartTime) / 1000000;
        latencies.add(latencyMs);
      }
    }
    long endTime = System.nanoTime();
    long durationMs = (endTime - startTime) / 1000000;
    client.stopConnection();
    double throughput = numRequests / (durationMs / 1000.0);
    double meanLatency = latencies.stream().mapToLong(Long::longValue).average().orElse(0);
    System.out.println("Sent " + numRequests + " requests in " + durationMs + "ms, throughput: " + throughput + " requests/s, mean latency: " + meanLatency + " ms");
  }

  @Test
  public void testCreate() {
    Map<String, Double> create = Map.of("1", 1000.0, "2", 2000.0, "3", 3000.0, "4", 4000.0, "5", 5000.0, "6", 6000.0, "7", 7000.0, "8", 8000.0, "9", 9000.0, "10", 10000.0);
    Map<String, Map<String, Double>> positions = Map.of("A", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "B", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "C", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "D", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "E", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "F", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0));
    sendCreateRequest(create, positions);
  }




  private List<List<SubTransactionRequest>> testMultiTrans() {
// Generate 10 sub-transactions
    List<List<SubTransactionRequest>> subTransactionRequests = new ArrayList<>();
    for (int i = 1; i < 11; i++) {
      List<SubTransactionRequest> orders = new ArrayList<>();

      // Generate 3-5 orders per sub-transaction
      int numOrders = (int) (Math.random() * 3) + 3;
      for (int j = 0; j < numOrders; j++) {
        String symbol = "";
        int amount = 0;
        double price = 0.0;

        // Randomly choose a symbol, amount, and price for each order
        switch ((int) (Math.random() * 3)) {
          case 0:
            symbol = "BTC";
            break;
          case 1:
            symbol = "AMD";
            break;
          case 2:
            symbol = "APPL";
            break;
        }
        amount = (int) (Math.random() * 100) - 50;
        price = Math.random() * 10 + 10;

        orders.add(new OrderRequest(Integer.toString(j), symbol, amount, price));
      }
      subTransactionRequests.add(orders);
      sendTransaction(String.valueOf(i), orders);

    }
    return subTransactionRequests;
  }


  @Test
  public void testMultipleCreate() {
    int numIteration = 100;
    for (int i = 1; i < numIteration; i++) {
      sendCreateRequest(Map.of(String.valueOf(i),
              (double) i * 1000), Map.of("APPL", Map.of(String.valueOf(i),
              (double) i * 1000), "BTC", Map.of(String.valueOf(i),
              (double) i * 1000), "AMD", Map.of(String.valueOf(i),
              (double) i * 1000)));
    }
  }

  @Test
  public void testTrans() {
    List<SubTransactionRequest> subTransactionRequests =
            List.of(new OrderRequest("1", "BTC", 100, 11.0), new OrderRequest("2", "APPL", 30, 12.0), new OrderRequest("3", "AMD", -20, 14.0), new OrderRequest("4", "APPL", -50, 17.0), new OrderRequest("5", "BTC", 40, 10.0), new OrderRequest("6", "AMD", 30, 16.0), new OrderRequest("7", "AMD", -60, 18.0), new OrderRequest("8", "BTC", -25, 15.0), new OrderRequest("9", "AMD", 40, 14.0), new OrderRequest("10", "APPL", -100, 15.0));
    sendTransaction("1", subTransactionRequests);
  }
}
