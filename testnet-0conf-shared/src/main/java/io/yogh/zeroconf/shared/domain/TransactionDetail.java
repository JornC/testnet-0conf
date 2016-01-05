package io.yogh.zeroconf.shared.domain;

import java.io.Serializable;

public class TransactionDetail implements Serializable {
  private static final long serialVersionUID = -5230934399747974590L;

  private TransactionCategory category;
  private double amount;
  private String address;

  private String originTxid;

  public TransactionDetail(final TransactionCategory category, final String address, final double amount, final String originTxid) {
    this.setOriginTxid(originTxid);
    this.setAddress(address);
    this.category = category;
    this.amount = amount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    long temp;
    temp = Double.doubleToLongBits(amount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((originTxid == null) ? 0 : originTxid.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final TransactionDetail other = (TransactionDetail) obj;
    if (address == null) {
      if (other.address != null)
        return false;
    } else if (!address.equals(other.address))
      return false;
    if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
      return false;
    if (category != other.category)
      return false;
    if (originTxid == null) {
      if (other.originTxid != null)
        return false;
    } else if (!originTxid.equals(other.originTxid))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TransactionDetail [category=" + category + ", amount=" + amount + "]";
  }

  public TransactionCategory getCategory() {
    return category;
  }

  public void setCategory(final TransactionCategory category) {
    this.category = category;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(final double amount) {
    this.amount = amount;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public String getOriginTxid() {
    return originTxid;
  }

  public void setOriginTxid(final String originTxid) {
    this.originTxid = originTxid;
  }
}
