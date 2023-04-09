import org.example.UserController;
import org.example.requests.sub_transaction_requests.CancelRequest;
import org.example.requests.sub_transaction_requests.OrderRequest;
import org.example.requests.sub_transaction_requests.QueryRequest;
import org.example.requests.sub_transaction_requests.SubTransactionRequest;
import org.junit.jupiter.api.Test;

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
  public void test() {
    Map<String, Double> create = Map.of("1", 1000.0, "2", 2000.0, "3", 3000.0, "4", 4000.0, "5", 5000.0, "6", 6000.0, "7", 7000.0, "8", 8000.0, "9", 9000.0, "10", 10000.0);
    Map<String, Map<String, Double>> positions = Map.of("A", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "B", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "C", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "D", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "E", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0),
            "F", Map.of("1", 100.0, "2", 200.0, "3", 300.0, "4", 400.0, "5", 500.0, "6", 600.0, "7", 700.0, "8", 800.0, "9", 900.0, "10", 1000.0));
    sendCreateRequest(create, positions);
  }

  @Test
  public void testTrans() {
    List<SubTransactionRequest> subTransactionRequests =
            List.of(new OrderRequest("1", "A", 100, 10.0), new OrderRequest("2", "A", 100, 10.0), new OrderRequest("3", "A", 100, 10.0), new OrderRequest("4", "A", 100, 10.0), new OrderRequest("5", "A", 100, 10.0), new OrderRequest("6", "A", 100, 10.0), new OrderRequest("7", "A", 100, 10.0), new OrderRequest("8", "A", 100, 10.0), new OrderRequest("9", "A", 100, 10.0), new OrderRequest("10", "A", 100, 10.0));

    sendTransaction("1", subTransactionRequests);


  }
}
