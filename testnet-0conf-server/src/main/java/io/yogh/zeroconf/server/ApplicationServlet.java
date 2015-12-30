package io.yogh.zeroconf.server;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import io.yogh.zeroconf.shared.error.ApplicationException;
import io.yogh.zeroconf.shared.service.ApplicationService;

@WebServlet("/application/application-service")
public class ApplicationServlet extends RemoteServiceServlet implements ApplicationService {
  private static final long serialVersionUID = 4743886889268257636L;

  @Override
  public String getPaymentAddress() throws ApplicationException {
    return NodeConnectionFactory.get().getPaymentAddress(getThreadLocalRequest().getSession());
  }
}
