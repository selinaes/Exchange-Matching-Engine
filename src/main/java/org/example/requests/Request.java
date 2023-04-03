package org.example.requests;

import org.example.models.Account;
import org.example.models.Position;
import org.example.results.Result;
import org.hibernate.Session;
import org.w3c.dom.Element;

import java.util.List;

public interface Request {
  Result execute();
}

