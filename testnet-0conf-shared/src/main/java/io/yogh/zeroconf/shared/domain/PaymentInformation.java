package io.yogh.zeroconf.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaymentInformation implements Serializable, IsSerializable {
  private static final long serialVersionUID = 8143369912185048744L;

  private String product;
  private ArrayList<String> transactions;

  // Default constructor for GWT.
  public PaymentInformation() {
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(final String product) {
    this.product = product;
  }

  public ArrayList<String> getTransactions() {
    return transactions;
  }

  public void setTransactions(final ArrayList<String> transactions) {
    this.transactions = transactions;
  }

  @Override
  public String toString() {
    return "PaymentInformation [product=" + product + ", transactions=" + transactions + "]";
  }
}
