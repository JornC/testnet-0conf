package io.yogh.zeroconf.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Well. Heh.
 *
 * What we have here is a publicly reachable internal wallet-notify handle.
 *
 * Go figure.
 *
 * Seeing that this is a testnet PoC - I shall not be handing out shits over
 * this. Also, really the only thing this is vulnerable to is DDoS, which
 * doesn't really make it stand out from all the rest.
 *
 * Put in place the most rudimentary form of checking anyway, so: ship it.
 */
@WebServlet("/internal/wallet-notify")
public class PaymentReceivedServlet extends HttpServlet {
  private static final long serialVersionUID = -2751382772619334250L;

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    // If the remote host doesn't match what we know the remote host is - bug
    // out. (this will break aliases / domains, but who gives a hoot)
    if (!System.getProperty("rpchost").equals(req.getRemoteHost())) {
      return;
    }

    NodeConnectionFactory.get().setPayment(req.getParameter("tx"));
  }
}
