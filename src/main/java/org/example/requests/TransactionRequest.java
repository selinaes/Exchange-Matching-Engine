package org.example.requests;

import org.example.requests.sub_create_requests.SubCreateRequest;
import org.w3c.dom.Element;

import java.util.List;

public class TransactionRequest implements Request {
  private List<SubCreateRequest> subCreateRequests;


  public TransactionRequest(Element element) {


  }

  @Override
  public void execute() {

  }
}
