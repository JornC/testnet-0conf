package io.yogh.zeroconf.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import io.yogh.zeroconf.client.widget.QRCodeWidget;
import io.yogh.zeroconf.client.widget.SwitchPanel;
import io.yogh.zeroconf.shared.domain.PaymentInformation;
import io.yogh.zeroconf.shared.service.ApplicationServiceAsync;

public class ApplicationRootView extends Composite {
  interface ApplicationRootViewUiBinder extends UiBinder<Widget, ApplicationRootView> {
  }

  private static final ApplicationRootViewUiBinder UI_BINDER = GWT.create(ApplicationRootViewUiBinder.class);

  private static final String MESSAGE_TO_SIGN = "I have purchased a digital signature of this text, using a 0-conf payment. ";

  @UiField SwitchPanel switchPanel;
  @UiField SpanElement signatureClearTextField;

  @UiField ParagraphElement addressTextField;
  @UiField QRCodeWidget addressQR;

  @UiField DivElement signatureSignedTextField;

  @UiField Label errorText;

  private final ApplicationServiceAsync service;

  private final String signText;
  private String addressText;

  private int counter = 0;

  @Inject
  public ApplicationRootView(final ApplicationServiceAsync service) {
    this.service = service;

    initWidget(UI_BINDER.createAndBindUi(this));

    signText = MESSAGE_TO_SIGN + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(new Date());
    signatureClearTextField.setInnerText(signText);

    switchPanel.showWidget(0);
  }

  @UiHandler("payButton")
  public void onPayButtonClick(final ClickEvent e) {
    refreshPaymentAddress();

    switchPanel.showWidget(1);
  }

  private void refreshPaymentAddress() {
    service.getPaymentAddress(new AsyncCallback<String>() {
      @Override
      public void onFailure(final Throwable caught) {
        switchPanel.showWidget(3);

        throw new RuntimeException(caught);
      }

      @Override
      public void onSuccess(final String result) {
        ApplicationRootView.this.setAddress(result);
      }
    });
  }

  protected void setAddress(final String result) {
    addressText = result;
    addressTextField.setInnerText(result);
    addressQR.setValue(result);

    // Start polling for payments
    doPaymentStatusPoll();
  }

  private void doPaymentStatusPoll() {
    if (counter > 500) {
      switchPanel.showWidget(5);
      return;
    }

    counter++;

    service.getPaymentStatus(signText, addressText, new AsyncCallback<PaymentInformation>() {
      @Override
      public void onFailure(final Throwable caught) {
        switchPanel.showWidget(3);
        throw new RuntimeException(caught);
      }

      @Override
      public void onSuccess(final PaymentInformation result) {
        if(result != null) {
          displayPayment(result);
        } else {
          Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            @Override
            public boolean execute() {
              doPaymentStatusPoll();

              return false;
            }
          }, 2000);
        }
      }
    });
  }

  private void displayPayment(final PaymentInformation result) {
    switchPanel.showWidget(2);
    signatureSignedTextField.setInnerText(result.getProduct());
  }
}
