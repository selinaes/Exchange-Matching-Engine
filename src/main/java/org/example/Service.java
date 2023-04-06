package org.example;


import org.example.models.Account;
import org.example.models.Order;
import org.example.models.Position;
import org.example.models.Symbol;
import org.example.requests.sub_create_requests.CreateAccount;
import org.example.requests.sub_create_requests.CreateSymbol;
import org.example.requests.sub_transaction_requests.CancelRequest;
import org.example.requests.sub_transaction_requests.OrderRequest;
import org.example.requests.sub_transaction_requests.QueryRequest;
import org.example.results.subResults.Canceled;
import org.example.results.subResults.Executed;
import org.example.results.subResults.Open;
import org.example.results.subResults.SubResult;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

//@Transactional
public class Service {
  //  Lock lock;
  //  @Transactional(value = Transactional.TxType.REQUIRED)
  public static Account createAccount(CreateAccount createAccount) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
//    SessionFactoryWrapper.ge
    Lock lock = SessionFactoryWrapper.getLock("account");
    lock.lock();
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
    } finally {
      lock.unlock();
    }
  }

  // add position to account

  public static Account createSymbol(CreateSymbol createSymbol) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    Lock lock = SessionFactoryWrapper.getLock("symbol");
    lock.lock();
    try (session) {
      Transaction tx = session.beginTransaction();
      Account account = session.get(Account.class, createSymbol.getAccountId());
      if (account == null) {
        throw new RequestException("Account does not exist");
      }
      addPosition(createSymbol.getName(), createSymbol.getAmount(), account, session);
      tx.commit();
      return account;
    } finally {
      lock.unlock();
    }
//    catch (TransactionException e) {
//      return createSymbol(createSymbol);
//    }
  }

  // must be called within a transaction

  private static Position addPosition(String sym, double amount, @NotNull Account account, Session session) throws RequestException {
//    Account account = session.get(Account.class, accountId);

//    if (account == null) {
//      throw new RequestException("Account does not exist");
//    }
    for (Position p : account.getPositions()) {
      if (p.getSymbol().equals(sym)) {
        p.setQuantity(p.getQuantity() + amount);
        session.save(p);
        session.save(account);
        return p;
      }
    }
    // create symbol
    Symbol symbol = addOrGetSymbol(sym, session);
    // create position
    Position position = new Position();
    position.setSymbol(sym);
    position.setQuantity(amount);
    position.setAccount(account);
//    session.saveOrUpdate(symbol);
    session.save(position);
    session.save(account);
    return position;

  }

  private static Symbol addOrGetSymbol(String sym, Session session) {
//    this.lock.lock();

    Symbol symbol = session.get(Symbol.class, sym);
    if (symbol != null) {
      return symbol;
    }
    Symbol newSymbol = new Symbol();
    newSymbol.setSymbol(sym);
    session.save(newSymbol);
//    session.flush();
    return newSymbol;
  }

  private static Symbol addSymbol(String sym) {
    Session session = SessionFactoryWrapper.openSession();
    try (session) {
      Transaction tx = session.beginTransaction();
      Symbol symbol = session.get(Symbol.class, sym);
      if (symbol != null) {
        return symbol;
      }
      Symbol newSymbol = new Symbol();
      newSymbol.setSymbol(sym);
      session.save(newSymbol);
//      session.flush();
      tx.commit();
      return newSymbol;
    }
  }


  public static Order createOrder(OrderRequest orderRequest) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();
    Lock lock = SessionFactoryWrapper.getLock("order");
    lock.lock();
//    synchronized (lock) {
//      lock.lock();
    try (session) {
//        session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
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
      // limit price is not positive
      if (orderRequest.getLimit() <= 0) {
        throw new RequestException("Can't open order with non-positive limit price");
      }

      if (orderRequest.getAmount() == 0) {
        throw new RequestException("Can't open order with 0 amount");
      }

      // check not enough shares or not enough money + subtract shares/money if enough
      if (orderRequest.getAmount() < 0) { // sell order, check shares, subtract shares
        for (Position p : account.getPositions()) {
          if (p.getSymbol().equals(sym.getSymbol())) {
            if (p.getQuantity() < orderRequest.getAmount() * -1) {
              throw new RequestException("You don't have enough shares for this sell order");
            } else {
              // add negative, same as subtract
              p.setQuantity(p.getQuantity() + orderRequest.getAmount());
            }
            session.save(p);
            session.save(account);
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
      // set parent to self
//      System.out.println("new order id: " + newOrder.getId());
      session.save(newOrder);
      newOrder.setParentId(newOrder.getId());
      session.save(newOrder);
//      System.out.println("new order id after save: " + newOrder.getId());
      session.save(account);
      tx.commit();
//      System.out.println("new order id after commit: " + newOrder.getId());
      return newOrder;
    } finally {
      lock.unlock();
    }
//    }
  }

  // earliest, closest to limit, same symbol, not by same account
  public static void executeMatching(Order newOrder) throws RequestException {
    Session session = SessionFactoryWrapper.openSession();

    Lock lock = SessionFactoryWrapper.getLock("order");
    lock.lock();
    try (session) {
//      session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
      Transaction tx = session.beginTransaction();

      List<Order> orders;
      do {
        orders = filterCompatibleList(newOrder, session);
        System.out.println("size: " + orders.size());
        System.out.println("tttttt" + orders);
        if (orders.size() == 0) {
          break;
        }
        Order match = findBestMatchInList(newOrder, orders);
        if (newOrder.getAmount() > 0) { // newOrder is buy order
          executeBuyToSell(newOrder, match, session);
        } else {
          executeBuyToSell(match, newOrder, session);
        }
      }
      while (newOrder.getStatus().equals(Order.Status.OPEN) && orders.size() != 0);
      // will stop if all executed, or if no other match could be found

      tx.commit();
    } finally {
      lock.unlock();
    }
  }

  // must be called within a transaction
  private static void executeBuyToSell(Order buyOrder, Order sellOrder, Session
          session) throws RequestException {
    // Execute matching
    // find execution price, which is the earlier one
    double executionPrice = sellOrder.getTime() < buyOrder.getTime() ?
            sellOrder.getLimitPrice() : buyOrder.getLimitPrice();
    Lock lock = SessionFactoryWrapper.getLock("account");
    lock.lock();
    try {
      // split order if needed
      double fulfilledAmount;
      if (buyOrder.getAmount() > sellOrder.getAmount() * -1) {
//      System.out.println("buy order amount > sell order amount");
//      System.out.println("buy order: " + buyOrder);
//      System.out.println("sell order: " + sellOrder);
        splitExecuteOrder(buyOrder, sellOrder.getAmount(), executionPrice, session);
        sellOrder.setStatus(Order.Status.EXECUTED);
        sellOrder.setExecutedPrice(executionPrice);
        sellOrder.setTimeToNow();
        session.update(sellOrder);
        fulfilledAmount = sellOrder.getAmount() * -1;
      } else if (buyOrder.getAmount() < sellOrder.getAmount() * -1) {
//      System.out.println("buy order amount < sell order amount");
//      System.out.println("buy order: " + buyOrder);
//      System.out.println("sell order: " + sellOrder);
        splitExecuteOrder(sellOrder, buyOrder.getAmount(), executionPrice, session);
        buyOrder.setStatus(Order.Status.EXECUTED);
        buyOrder.setExecutedPrice(executionPrice);
        buyOrder.setTimeToNow();
        session.update(buyOrder);
        fulfilledAmount = buyOrder.getAmount();
      } else { // change both status to executed, if no split
//      System.out.println("buy order amount = sell order amount");
//      System.out.println("buy order: " + buyOrder);
//      System.out.println("sell order: " + sellOrder);
        buyOrder.setStatus(Order.Status.EXECUTED);
        buyOrder.setExecutedPrice(executionPrice);
        buyOrder.setTimeToNow();
        sellOrder.setStatus(Order.Status.EXECUTED);
        sellOrder.setExecutedPrice(executionPrice);
        sellOrder.setTimeToNow();
        session.update(buyOrder);
        session.update(sellOrder);
        fulfilledAmount = buyOrder.getAmount();
      }

      // find seller and buyer
      Account seller = sellOrder.getAccount();
      Account buyer = buyOrder.getAccount();

      // adding money to seller account
      seller.setBalance(seller.getBalance() + fulfilledAmount * executionPrice);
      session.update(seller);
      session.update(buyer);
      // new position / add share to buyer account
      addPosition(buyOrder.getSymbol(), fulfilledAmount, buyer, session);
    } finally {
      lock.unlock();
    }
  }

  // must inside transaction!
  private static void splitExecuteOrder(Order toSplit, double splitAmount,
                                        double executePrice, Session session) {
    // child, execute
    Order splitted = new Order();
    splitted.setParentId(toSplit.getId());
    splitted.setLimitPrice(toSplit.getLimitPrice());
    splitted.setSymbol(toSplit.getSymbol());
    splitted.setAccount(toSplit.getAccount());
    splitted.setAmount(-splitAmount);
    splitted.setStatus(Order.Status.EXECUTED);
    splitted.setExecutedPrice(executePrice);
    session.save(splitted);

    // parent, remain open. splitAmount is always positive adjustment
    toSplit.setAmount(toSplit.getAmount() + splitAmount);
//    if (toSplit.getAmount() > 0) {
//      toSplit.setAmount(toSplit.getAmount() + splitAmount);
//    } else {
//      toSplit.setAmount(toSplit.getAmount() + splitAmount);
//    }

    session.update(toSplit);
  }

  // make sure orders size > 0
  private static Order findBestMatchInList(Order newOrder, List<Order> orders) {
    Order bestMatch = null;

    // get the best price match
    if (newOrder.getAmount() < 0) { // sell, looking for highest buy
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

    return bestMatch;
  }

  // must inside a transaction
  private static List<Order> filterCompatibleList(Order newOrder, Session session) {

    // same symbol, smaller amount, higher price, not by same account
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Order> query = builder.createQuery(Order.class);
    Root<Order> root = query.from(Order.class);
//    query = query.where(
//            builder.equal(root.get("symbol"), newOrder.getSymbol()),
//            builder.notEqual(root.get("account").get("id"), newOrder.getAccount().getId()),
//            builder.equal(root.get("status"), Order.Status.OPEN)
//    );

    if (newOrder.getAmount() > 0) { // buy
      query.where(
              builder.equal(root.get("symbol"), newOrder.getSymbol()),
              builder.notEqual(root.get("account").get("id"), newOrder.getAccount().getId()),
              builder.equal(root.get("status"), Order.Status.OPEN),
              builder.lt(root.get("amount"), 0), // buy looking for sell, amount < 0
              builder.lessThanOrEqualTo(root.get("limitPrice"), newOrder.getLimitPrice())); // match <= newOrder price
    } else if (newOrder.getAmount() < 0) { // sell
      query.where(
              builder.equal(root.get("symbol"), newOrder.getSymbol()),
              builder.notEqual(root.get("account").get("id"), newOrder.getAccount().getId()),
              builder.equal(root.get("status"), Order.Status.OPEN),
              builder.gt(root.get("amount"), 0), // sell looking for buy, amount > 0
              builder.greaterThanOrEqualTo(root.get("limitPrice"), newOrder.getLimitPrice())); // match >= newOrder price
    }

    return session.createQuery(query).getResultList();
  }


  public static List<SubResult> queryOrder(QueryRequest queryRequest) throws
          RequestException {
    Session session = SessionFactoryWrapper.openSession();
//    session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
    List<SubResult> subResults = new ArrayList<>();
    Lock lock = SessionFactoryWrapper.getLock("order");
    lock.lock();
    try (session) {
//      session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
      Transaction tx = session.beginTransaction();

      Order parentOrder = session.get(Order.class, queryRequest.getTransactionId());
      if (parentOrder == null) { // if orderId not valid, error
        tx.rollback();
        throw new RequestException("Transaction ID not valid");
      } else if (!parentOrder.getAccount().getId().equals(queryRequest.getAccountId())) {
        // verify that the account belong to querying account, otherwise error
        tx.rollback();
        throw new RequestException("This transaction does not belong to your account");
      }

      // find orders with matching id (order's parentId match request's transactionId)
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Order> query = builder.createQuery(Order.class);
      Root<Order> root = query.from(Order.class);
      query.where(
              builder.equal(root.get("parentId"), parentOrder.getId())
      );

      List<Order> orders = session.createQuery(query).getResultList();
      System.out.println("childOrders: " + orders);
      for (Order order : orders) {
        SubResult subResult;
        if (order.getStatus() == Order.Status.OPEN) {
          subResult = new Open();
          subResult.addAttribute("shares", String.valueOf(order.getAmount()));
        } else if (order.getStatus() == Order.Status.CANCELLED) {
          subResult = new Canceled();
          subResult.addAttribute("shares", String.valueOf(order.getAmount()));
          subResult.addAttribute("time", String.valueOf(order.getTime()));
        } else {
          subResult = new Executed();
          subResult.addAttribute("shares", String.valueOf(order.getAmount()));
          subResult.addAttribute("time", String.valueOf(order.getTime()));
          subResult.addAttribute("price", String.valueOf(order.getExecutedPrice())); // actual executed price?
        }
        subResults.add(subResult);
      }
      tx.commit();
    } finally {
      lock.unlock();
    }
    return subResults;
  }


  public static List<SubResult> cancelOrder(CancelRequest cancelRequest) throws
          RequestException {
    Session session = SessionFactoryWrapper.openSession();
//    session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
    List<SubResult> subResults = new ArrayList<>();
    Lock lock = SessionFactoryWrapper.getLock("order");
    lock.lock();
    try (session) {
//      session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));
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
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.where(
                builder.equal(root.get("parentId"), cancelRequest.getOrderId()),
                builder.equal(root.get("status"), Order.Status.OPEN));

      List<Order> orders = session.createQuery(query).getResultList();

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
      query.where(
              builder.equal(root.get("parentId"), cancelRequest.getOrderId()),
              builder.equal(root.get("status"), Order.Status.EXECUTED));
      List<Order> executedOrders = session.createQuery(query).getResultList();

      for (Order order : executedOrders) {
        SubResult subResult = new Executed();
        subResult.addAttribute("shares", String.valueOf(order.getAmount()));
        subResult.addAttribute("price", String.valueOf(order.getExecutedPrice())); // actual executed price?
        subResult.addAttribute("time", String.valueOf(order.getTime()));
        subResults.add(subResult);
      }
      tx.commit();
    } finally {
      lock.unlock();
    }
    return subResults;
  }

}
