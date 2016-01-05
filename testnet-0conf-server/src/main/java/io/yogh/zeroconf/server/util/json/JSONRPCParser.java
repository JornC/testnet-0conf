package io.yogh.zeroconf.server.util.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import io.yogh.zeroconf.shared.domain.TransactionCategory;
import io.yogh.zeroconf.shared.domain.TransactionDetail;

public class JSONRPCParser {
  private JSONRPCParser() {
  }

  public static String getString(final InputStream jsonData) throws JsonProcessingException, IOException {
    final JsonNode tree = JsonParser.mapper.readTree(jsonData);

    return tree.getTextValue();
  }

  public static String getResultString(final InputStream jsonData) throws JsonProcessingException, IOException {
    final JsonNode tree = JsonParser.mapper.readTree(jsonData);

    return tree.get("result").getTextValue();
  }

  private static JsonNode getResultNode(final InputStream jsonData) throws JsonProcessingException, IOException {
    return JsonParser.mapper.readTree(jsonData).get("result");
  }

  public static ArrayList<TransactionDetail> getTransactionInformation(final InputStream jsonData) throws JsonProcessingException, IOException {
    final JsonNode tree = getResultNode(jsonData);

    final ArrayList<TransactionDetail> transactionInformation = new ArrayList<>();
    
    String txid = tree.get("txid").getTextValue();

    for(JsonNode detail : tree.get("details")) {
      TransactionCategory cat = TransactionCategory.fromValue(detail.get("category").getTextValue());
      double amount = detail.get("amount").getDoubleValue();
      String address = detail.get("address").getTextValue();
      
      transactionInformation.add(new TransactionDetail(cat, address, amount, txid));
    }
    
    return transactionInformation;
  }

  public static boolean isNullResult(final InputStream jsonData) throws JsonProcessingException, IOException {
    return getResultNode(jsonData).isNull();
  }
}
