package io.yogh.zeroconf.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FieldSetElement;
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
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import io.yogh.zeroconf.client.widget.QRCodeWidget;
import io.yogh.zeroconf.shared.service.ApplicationServiceAsync;

public class ApplicationRootView extends Composite {
  interface ApplicationRootViewUiBinder extends UiBinder<Widget, ApplicationRootView> {
  }

  private static final ApplicationRootViewUiBinder UI_BINDER = GWT.create(ApplicationRootViewUiBinder.class);

  private static final String MESSAGE_TO_SIGN = "I have purchased a digital signature of this text, using a 0-conf payment. ";

  @UiField DeckPanel switchPanel;
  @UiField SpanElement signatureTextField;
 
  @UiField ParagraphElement addressText;
  @UiField QRCodeWidget addressQR;

  @UiField Label errorText;

  private final ApplicationServiceAsync service;

  @Inject
  public ApplicationRootView(final ApplicationServiceAsync service) {
    this.service = service;

    initWidget(UI_BINDER.createAndBindUi(this));

    signatureTextField.setInnerText(MESSAGE_TO_SIGN + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT).format(new Date()));
    
    switchPanel.showWidget(0);
  }

  @UiHandler("payButton")
  public void onPayButtonClick(final ClickEvent e) {
    refreshPaymentAddress();

    switchPanel.showWidget(1);
  }

  private void refreshPaymentAddress() {
    errorText.setVisible(false);
    service.getPaymentAddress(new AsyncCallback<String>() {
      @Override
      public void onFailure(final Throwable caught) {
        errorText.setVisible(true);

        throw new RuntimeException(caught);
      }

      @Override
      public void onSuccess(final String result) {
        addressText.setInnerText(result);
        addressQR.setValue(result);
      }
    });
  }
}
