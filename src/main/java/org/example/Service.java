package org.example;

import org.example.models.Account;
import org.example.models.Order;
import org.example.models.Position;
import org.example.models.Symbol;
import org.example.requests.sub_create_requests.CreateAccount;
import org.example.requests.sub_create_requests.CreateSymbol;
import org.example.requests.sub_transaction_requests.CancelRequest;
import org.example.requests.sub_transaction_requests.OrderRequest;
import org.example.results.subResults.Canceled;
import org.example.results.subResults.Executed;
import org.example.results.subResults.SubResult;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

public class Service {

  public static Account createAccount(CreateAccount createAccount) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    try (session) {
      Transaction tx = session.beginTransaction();
      if (session.get(Account.class, createAccount.getId()) != null) {
        throw new RequestException("Account already exists");
      }
      Account account = new Account();
      account.setId(createAccount.getId());
      account.setBalance(createAccount.getBalance());
      session.save(account);
      tx.commit();
      return account;
    }
  }

  public static Account createSymbol(CreateSymbol createSymbol) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    try (session) {
      Transaction tx = session.beginTransaction();
      Account account = session.get(Account.class, createSymbol.getAccountId());
      if (account == null) {
        throw new RequestException("Account does not exist");
      }
      addPosition(createSymbol.getName(), createSymbol.getAmount(), createSymbol.getAccountId(), session);
      tx.commit();
      return account;
    }
  }

  // must be called within a transaction
  private static Position addPosition(String sym, double amount, String accountId, Session session) throws RequestException {
    Account account = session.get(Account.class, accountId);
    if (account == null) {
      throw new RequestException("Account does not exist");
    }
    for (Position p : account.getPositions()) {
      if (p.getSymbol().equals(sym)) {
        p.setQuantity(p.getQuantity() + amount);
        session.save(p);
        session.save(account);
        return p;
      }
    }
    // create symbol
    Symbol symbol = session.get(Symbol.class, sym);
    if (symbol == null) {
      symbol = new Symbol();
      symbol.setSymbol(sym);
      session.save(symbol);
    }
    // create position
    Position position = new Position();
    position.setSymbol(sym);
    position.setQuantity(amount);
    position.setAccount(account);
    session.save(position);

    session.save(account);
    return position;

  }

//  private static Symbol createSymbol(String sym, Session session) {
//    Transaction tx = session.beginTransaction();
//    Symbol symbol = new Symbol();
//    symbol.setSymbol(sym);
//    session.save(symbol);
//    tx.commit();
//    return symbol;
//  }


//  private static Account getAccountById(String id) {
////    try (Session session = SessionFactoryWrapper.openSession()) {
////      Transaction tx = session.beginTransaction();
//      Account account = session.get(Account.class, id);
//      tx.commit();
//      return account;
//    }
//  }


  public static Order createOrder(OrderRequest orderRequest) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    try (session) {
      Transaction tx = session.beginTransaction();
      Account account = session.get(Account.class, orderRequest.getAccountId());
      // account not exist
      if (account == null) {
        throw new RequestException("Account does not exist");
      }
      // symbol not exist
      Symbol sym = session.get(Symbol.class, orderRequest.getSymbol());
      if (sym == null) {
        throw new RequestException("Symbol does not exist");
      }
      // limit price is 0
      if (orderRequest.getLimit() == 0) {
        throw new RequestException("Can't open order with limit price 0");
      }

      // check not enough shares or not enough money + subtract shares/money if enough
      if (orderRequest.getLimit() < 0) { // sell order, check shares, subtract shares
        for (Position p : account.getPositions()) {
          if (p.getSymbol().equals(sym)) {
            if (p.getQuantity() < orderRequest.getAmount()) {
              throw new RequestException("You don't have enough shares for this sell order");
            } else {
              p.setQuantity(p.getQuantity() - orderRequest.getAmount());
            }
            session.save(p);
          }
        }
      } else { // buy order, check money, subtract money
        if (account.getBalance() < orderRequest.getLimit() * orderRequest.getAmount()) {
          throw new RequestException("You don't have enough money for this buy order");
        } else {
          account.setBalance(
                  account.getBalance() - orderRequest.getLimit() * orderRequest.getAmount());
          session.save(account);
        }
      }

      // create order
      Order newOrder = new Order();
      newOrder.setAmount(orderRequest.getAmount());
      newOrder.setLimitPrice(orderRequest.getLimit());
      newOrder.setSymbol(orderRequest.getSymbol());
      newOrder.setAccount(account);
      newOrder.setParentId(newOrder.getId()); // set parent to self

      session.save(newOrder);
      session.save(account);
      tx.commit();
      return newOrder;
    }
  }


  // earliest, closest to limit, same symbol, not by same account
  public static void executeMatching(Order newOrder) throws RequestException{
    Session session = SessionFactoryWrapper.openSession();

    try (session) {
      Transaction tx = session.beginTransaction();

      List<Order> orders;
      do {
        orders = filterCompatibleList(newOrder, session);
        Order match = findBestMatchInList(newOrder, orders);
        executeOneToOne(newOrder, match, session);
      }
      while (newOrder.getStatus() == Order.Status.OPEN && orders.size() != 0);
      // will stop if all executed, or if no other match could be found

      tx.commit();
    }
  }

  // must be called within a transaction
  private static void executeOneToOne(Order newOrder, Order match, Session session) throws RequestException{
    // Execute matching
    // split order if needed
    double fulfilledAmount;
    if (match.getAmount() > newOrder.getAmount()){
      splitExecuteOrder(match, newOrder.getAmount(), session);
      newOrder.setStatus(Order.Status.EXECUTED);
      session.save(newOrder);
      fulfilledAmount = newOrder.getAmount();
    } else if (match.getAmount() < newOrder.getAmount()) {
      splitExecuteOrder(newOrder, match.getAmount(), session);
      match.setStatus(Order.Status.EXECUTED);
      session.save(match);
      fulfilledAmount = match.getAmount();
    } else { // change both status to executed, if no split
      match.setStatus(Order.Status.EXECUTED);
      newOrder.setStatus(Order.Status.EXECUTED);
      session.save(match);
      session.save(newOrder);
      fulfilledAmount = match.getAmount();
    }

    // find seller and buyer
    Account seller;
    Account buyer;
    if (match.getLimitPrice() < 0 ) { // match is seller, newOrder is buyer
      seller = match.getAccount();
      buyer = newOrder.getAccount();
    } else { // match is buyer, newOrder is seller
      seller = newOrder.getAccount();
      buyer = match.getAccount();
    }

    // adding money to seller account
    seller.setBalance(seller.getBalance() + fulfilledAmount * match.getLimitPrice());
    session.save(seller);
    // new position / add share to buyer account
    addPosition(match.getSymbol(), fulfilledAmount, buyer.getId(), session);
  }

  // must inside transaction!
  private static void splitExecuteOrder(Order toSplit, double splitAmount, Session session){
    // child, execute
    Order splitted = new Order();
    splitted.setParentId(toSplit.getId());
    splitted.setLimitPrice(toSplit.getLimitPrice());
    splitted.setSymbol(toSplit.getSymbol());
    splitted.setAccount(toSplit.getAccount());
    splitted.setAmount(splitAmount);
    splitted.setStatus(Order.Status.EXECUTED);
    session.save(splitted);

    // parent, remain open
    toSplit.setAmount(toSplit.getAmount() - splitAmount);
    session.save(toSplit);
  }

  private static Order findBestMatchInList(Order newOrder, List<Order> orders) {
    Order bestMatch = null;
    if (orders.size() > 0) {
      // get the best price match
      if (newOrder.getLimitPrice() > 0) { // sell, looking for highest buy
        for (Order order : orders) {
          // no bestMatch OR price higher OR same price time earlier
          if (bestMatch == null || order.getLimitPrice() > bestMatch.getLimitPrice() ||
                  (order.getLimitPrice() == bestMatch.getLimitPrice() &&
                          order.getTime() < bestMatch.getTime())) {
            bestMatch = order;
          }
        }
      } else { // buy, looking for lowest sell
        for (Order order : orders) {
          if (bestMatch == null || order.getLimitPrice() < bestMatch.getLimitPrice() ||
                  (order.getLimitPrice() == bestMatch.getLimitPrice() &&
                          order.getTime() < bestMatch.getTime())) {
            bestMatch = order;
          }
        }
      }
    }
    return bestMatch;
  }

  // must inside a transaction
  private static List<Order> filterCompatibleList(Order newOrder, Session session) {

    // same symbol, smaller amount, higher price, not by same account
    Criteria criteria = session.createCriteria(Order.class);
    criteria.add(Restrictions.eq("symbol", newOrder.getSymbol()));
    criteria.add(Restrictions.ge("limitPrice",
            newOrder.getLimitPrice() * -1)); // -S looking for >S, B looking for >-B
    criteria.createAlias("account", "a").add(Restrictions.ne("a.id", newOrder.getAccount().getId()));
    criteria.add(Restrictions.eq("status", "OPEN"));

    if (newOrder.getLimitPrice() > 0) {
      criteria.add(Restrictions.lt("limitPrice", 0));
    } else if (newOrder.getLimitPrice() < 0) {
      criteria.add(Restrictions.gt("limitPrice", 0));
    }

    List<Order> orders = criteria.list();

    return orders;
  }


  public static List<SubResult> cancelOrder(CancelRequest cancelRequest) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    List<SubResult> subResults = new ArrayList<>();
    try (session) {
      Transaction tx = session.beginTransaction();

      Order parentOrder = session.get(Order.class, cancelRequest.getOrderId());
      if (parentOrder == null) { // if orderId not valid, error
        tx.rollback();
        throw new RequestException("Transaction ID not valid");
      } else if (!parentOrder.getAccount().getId().equals(cancelRequest.getAccountId())) {
        // verify that the account belong to querying account, otherwise error
        tx.rollback();
        throw new RequestException("This transaction does not belong to your account");
      }

      // find open order with matching id (order's parentId match request's transactionId)
      Criteria criteria = session.createCriteria(Order.class);
      criteria.add(Restrictions.eq("parentId", cancelRequest.getOrderId()));
      criteria.add(Restrictions.eq("status", "OPEN"));
      List<Order> orders = criteria.list();

      // if no open portion, error
      if (orders.size() == 0) {
        tx.rollback();
        throw new RequestException("No open portion of this transaction found. All executed or cancelled.");
      }
      // change the status of orders to cancelled, refund amount back to account
      for (Order order : orders) {
        order.setStatus(Order.Status.CANCELLED);
        order.setTimeToNow();
        Account owner = order.getAccount();
        if (order.getLimitPrice() > 0) { // buy, refund money
          owner.setBalance(owner.getBalance() + order.getAmount() * order.getLimitPrice());
        } else { // sell, refund shares
          Position position = owner.getPositionBySym(order.getSymbol());
          position.setQuantity(position.getQuantity() + order.getAmount());
        }
        SubResult subResult = new Canceled();
        subResult.addAttribute("shares", String.valueOf(order.getAmount()));
        subResult.addAttribute("time", String.valueOf(order.getTime()));
        subResults.add(subResult);
      }

      // get all executed portion of the order
      criteria = session.createCriteria(Order.class);
      criteria.add(Restrictions.eq("parentId", cancelRequest.getOrderId()));
      criteria.add(Restrictions.eq("status", "EXECUTED"));
      List<Order> executedOrders = criteria.list();
      for (Order order : executedOrders) {
        SubResult subResult = new Executed();
        subResult.addAttribute("shares", String.valueOf(order.getAmount()));
        subResult.addAttribute("price", String.valueOf(order.getLimitPrice())); // actual executed price?
        subResult.addAttribute("time", String.valueOf(order.getTime()));
        subResults.add(subResult);
      }
      tx.commit();
    }
    return subResults;
  }

}
