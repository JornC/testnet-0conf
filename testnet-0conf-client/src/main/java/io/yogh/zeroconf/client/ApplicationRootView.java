package io.yogh.zeroconf.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import io.yogh.zeroconf.shared.service.ApplicationServiceAsync;

public class ApplicationRootView extends Composite {
  interface ApplicationRootViewUiBinder extends UiBinder<Widget, ApplicationRootView> {
  }

  private static final ApplicationRootViewUiBinder UI_BINDER = GWT.create(ApplicationRootViewUiBinder.class);

  @UiField HTMLPanel payContainer;

  @UiField Label address;

  private final ApplicationServiceAsync service;

  @Inject
  public ApplicationRootView(final ApplicationServiceAsync service) {
    this.service = service;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @UiHandler("payButton")
  public void onPayButtonClick(final ClickEvent e) {
    if (!payContainer.isVisible()) {
      refreshPaymentAddress();
    }

    payContainer.setVisible(true);
  }

  private void refreshPaymentAddress() {
    service.getPaymentAddress(new AsyncCallback<String>() {
      @Override
      public void onFailure(final Throwable caught) {

      }

      @Override
      public void onSuccess(final String result) {
        address.setText(result);
      }
    });
  }
}
