package io.yogh.zeroconf.server;

import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import io.yogh.zeroconf.server.providers.BitcoinJSONRPCRetriever;
import io.yogh.zeroconf.shared.domain.PaymentInformation;
import io.yogh.zeroconf.shared.domain.TransactionCategory;
import io.yogh.zeroconf.shared.domain.TransactionDetail;
import io.yogh.zeroconf.shared.error.ApplicationException;
import io.yogh.zeroconf.shared.service.ApplicationService;

@WebServlet("/application/application-service")
public class ApplicationServlet extends RemoteServiceServlet implements ApplicationService {
  private static final long serialVersionUID = 4743886889268257636L;

  private static final double PAYMENT_AMOUNT = 0.1;

  @Override
  public String getPaymentAddress() throws ApplicationException {
    return NodeConnectionFactory.get().getPaymentAddress();
  }

  @Override
  public PaymentInformation getPaymentStatus(final String signText, final String address) throws ApplicationException {
    final BitcoinJSONRPCRetriever node = NodeConnectionFactory.get();

    final HashSet<TransactionDetail> paymentDetails = node.getPaymentDetails(address);

    // Iterate over received payments - should be only 1, and only of type receive, but we're accounting for funny business.
    double sumAmount = 0;
    for(final TransactionDetail detail : paymentDetails) {
      if(detail.getCategory() != TransactionCategory.RECEIVE) {
        continue;
      }

      sumAmount += detail.getAmount();
    }

    // Check to see if this money is totally ours. Instant payment, right.
    if (sumAmount < PAYMENT_AMOUNT) {
      return null;
    }

    final PaymentInformation information = new PaymentInformation();

    information.setProduct(node.createSignature(signText, address));

    final ArrayList<String> txs = new ArrayList<String>();
    for(final TransactionDetail detail : paymentDetails) {
      if(detail.getCategory() != TransactionCategory.RECEIVE) {
        continue;
      }

      txs.add(detail.getOriginTxid());
    }

    information.setTransactions(txs);

    // Payment succesful - probably.
    node.clearPayment(address);

    System.out.println(information);

    return information;
  }
}
