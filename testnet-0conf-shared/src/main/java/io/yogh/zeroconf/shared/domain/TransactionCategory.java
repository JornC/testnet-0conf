package io.yogh.zeroconf.shared.domain;

public enum TransactionCategory {
  SEND, RECEIVE, WTF;

  public static TransactionCategory fromValue(final String cat) {
    try {
      return valueOf(cat.toUpperCase());
    } catch (Exception e) {
      return WTF;
    }
  }
}
