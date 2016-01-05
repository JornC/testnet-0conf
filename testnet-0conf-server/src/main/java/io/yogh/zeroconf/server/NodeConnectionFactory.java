package io.yogh.zeroconf.server;

import io.yogh.zeroconf.server.providers.BitcoinJSONRPCRetriever;

public class NodeConnectionFactory {
  private BitcoinJSONRPCRetriever hook;

  private static NodeConnectionFactory instance;
  
  public NodeConnectionFactory() {
    hook = new BitcoinJSONRPCRetriever(System.getProperties());
  }

  private static NodeConnectionFactory create() {
    if(instance == null) {
      instance = new NodeConnectionFactory();
    }

    return instance;
  }

  public static BitcoinJSONRPCRetriever get() {
    return create().hook;
  }
}