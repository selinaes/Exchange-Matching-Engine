package org.example.requests;

import org.example.requests.sub_create_requests.CreateAccount;
import org.example.requests.sub_create_requests.CreateSymbol;
import org.example.requests.sub_create_requests.SubCreateRequest;
import org.example.requests.sub_transaction_requests.SubTransactionRequest;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import java.util.ArrayList;
import java.util.List;


public class CreateRequest implements Request {
  private List<SubCreateRequest> subCreateRequests;


  public CreateRequest(Element element) {
    this.subCreateRequests = new ArrayList<>();
    NodeList nList = element.getChildNodes();
//    System.out.println(nList.getLength());
    for (int i = 0; i < nList.getLength(); i++) {
      if (nList.item(i).getNodeType() == Element.ELEMENT_NODE) {
        Element subElement = (Element) nList.item(i);

        System.out.println("main nood child: " + subElement.getNodeName());
//        DOMImplementationLS lsImpl = (DOMImplementationLS) subElement.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
//        LSSerializer serializer = lsImpl.createLSSerializer();
//        serializer.getDomConfig().setParameter("xml-declaration", false); //by default its true, so set it to false to get String without xml-declaration
//        String str = serializer.writeToString(subElement);
        this.subCreateRequests.addAll(subCreateRequestsBuilder(subElement));

      }
    }
  }


  private List<SubCreateRequest> subCreateRequestsBuilder(Element element) {
    System.out.println("subCreateRequestsBuilder: " + element.getNodeName());
    List<SubCreateRequest> res = new ArrayList<>();
    String accountId;
    switch (element.getNodeName()) {
      case "account":
        accountId = element.getAttribute("id");
        System.out.println(accountId);
        String balance = element.getAttribute("balance");
        System.out.println(balance);
        res.add(new CreateAccount(accountId, Double.parseDouble(balance)));
        break;
      case "symbol":
        String symbol = element.getAttribute("sym");
        NodeList symbolChildren = element.getChildNodes();
        for (int j = 0; j < symbolChildren.getLength(); j++) {
          if (symbolChildren.item(j).getNodeType() == Element.ELEMENT_NODE) {
            element = (Element) symbolChildren.item(j);
            accountId = element.getAttribute("id");
            double amount = element.getTextContent().equals("") ? 0 : Double.parseDouble(element.getTextContent());
            res.add(new CreateSymbol(symbol, accountId, amount));
          }
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid create request");
    }
    return res;
  }

  public List<SubCreateRequest> getSubCreateRequests() {
    return subCreateRequests;
  }

  @Override
  public void execute() {

  }
}
