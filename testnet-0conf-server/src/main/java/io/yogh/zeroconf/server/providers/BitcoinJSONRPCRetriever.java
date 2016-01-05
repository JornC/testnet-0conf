package io.yogh.zeroconf.server.providers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;

import io.yogh.zeroconf.server.util.HttpClientProxy;
import io.yogh.zeroconf.server.util.json.JSONRPCEncoder;
import io.yogh.zeroconf.server.util.json.JSONRPCParser;
import io.yogh.zeroconf.shared.domain.TransactionDetail;
import io.yogh.zeroconf.shared.error.ApplicationException;

public class BitcoinJSONRPCRetriever {
  private static final String JSON_RPC_REALM = "jsonrpc";
  private static final String AUTH_SCHEME = AuthSchemes.BASIC;

  private static final String URI_FORMAT = "http://%s:%s";

  private final String uri;

  private final HttpClientContext localContext;
  private final CredentialsProvider credentialsProvider = new SystemDefaultCredentialsProvider();

  private final HashMap<String, HashSet<TransactionDetail>> addressSessionMap = new HashMap<>();

  public BitcoinJSONRPCRetriever(final Properties config) {
    final String host = (String) config.get("rpchost");
    final Integer port = Integer.parseInt((String) config.get("rpcport"));
    final String rpcUser = (String) config.get("rpcuser");
    final String rpcPass = (String) config.get("rpcpass");

    uri = String.format(URI_FORMAT, host, port);
    credentialsProvider.setCredentials(new AuthScope(host, port, JSON_RPC_REALM, AUTH_SCHEME), new UsernamePasswordCredentials(rpcUser, rpcPass));

    final AuthCache authCache = new BasicAuthCache();
    authCache.put(new HttpHost(host, port), new BasicScheme());

    localContext = HttpClientContext.create();
    localContext.setAuthCache(authCache);
  }

  private String doSimpleJSONRPCMethod(final String method, final Object... params) throws IOException, HttpException {
    try (CloseableHttpClient client = getAuthenticatedHttpClientProxy();
        InputStream stream = doComplexJSONRPCMethod(client, method, params).getContent()) {
      return JSONRPCParser.getResultString(stream);
    }
  }

  private HttpEntity doComplexJSONRPCMethod(final CloseableHttpClient client, final String method, final Object... params)
      throws IOException, IllegalStateException, ParseException, HttpException {
    return doComplexJSONRPCMethod(client, method, false, params);
  }

  private HttpEntity doComplexJSONRPCMethod(final CloseableHttpClient client, final String method, final boolean unsafe, final Object... params)
      throws IOException, IllegalStateException, ParseException, HttpException {
    final String payload = JSONRPCEncoder.getRequestString(method, params);

    // Temporary
    System.out.println("> " + payload);

    return HttpClientProxy.postRemoteContent(client, unsafe, uri, payload);
  }

  private CloseableHttpClient getAuthenticatedHttpClientProxy() {
    return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
  }

  public String getPaymentAddress() throws ApplicationException {
    try {
      final String address = doSimpleJSONRPCMethod("getnewaddress");

      addressSessionMap.put(address, new HashSet<TransactionDetail>());

      return address;
    } catch (final IOException e) {
      throw new ApplicationException();
    } catch (final HttpException e) {
      throw new ApplicationException();
    }
  }

  public void setPayment(final String txid) {
    try (final CloseableHttpClient client = getAuthenticatedHttpClientProxy()) {
      final InputStream entity = doComplexJSONRPCMethod(client, "gettransaction", txid).getContent();

      final ArrayList<TransactionDetail> tx = JSONRPCParser.getTransactionInformation(entity);

      for(final TransactionDetail detail : tx) {
        consumeTransactionDetail(detail);
      }

    } catch (IllegalStateException | ParseException | IOException | HttpException e) {
      e.printStackTrace();
    }
  }

  private void consumeTransactionDetail(final TransactionDetail detail) {
    if(addressSessionMap.containsKey(detail.getAddress())) {
      addressSessionMap.get(detail.getAddress()).add(detail);
      System.out.println("Payment received: " + detail);
    }
  }

  public HashSet<TransactionDetail> getPaymentDetails(final String address) {
    return addressSessionMap.get(address);
  }

  public String createSignature(final String signText, final String address) throws ApplicationException {
    try {
      return doSimpleJSONRPCMethod("signmessage", address, signText);
    } catch (IOException | HttpException e) {
      throw new ApplicationException();
    }
  }

  public void clearPayment(final String address) {
    addressSessionMap.remove(address);
  }
}
