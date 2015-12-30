package io.yogh.zeroconf.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import io.yogh.zeroconf.shared.error.ApplicationException;

@RemoteServiceRelativePath("application-service")
public interface ApplicationService extends RemoteService {
  String getPaymentAddress() throws ApplicationException;
}
