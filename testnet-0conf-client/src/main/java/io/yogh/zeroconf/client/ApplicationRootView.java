package io.yogh.zeroconf.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import io.yogh.zeroconf.client.ui.VendorView;
import io.yogh.zeroconf.client.widget.SwitchPanel;

public class ApplicationRootView extends Composite {
  interface ApplicationRootViewUiBinder extends UiBinder<Widget, ApplicationRootView> {
  }

  private static final ApplicationRootViewUiBinder UI_BINDER = GWT.create(ApplicationRootViewUiBinder.class);

  @UiField SwitchPanel switchPanel;

  @UiField Button vendorButton;
  @UiField Button historyButton;

  @UiField(provided = true) VendorView vendor;

  @Inject
  public ApplicationRootView(final VendorView vendor) {
    this.vendor = vendor;

    initWidget(UI_BINDER.createAndBindUi(this));

    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      @Override
      public void onValueChange(final ValueChangeEvent<String> event) {
        final String historyToken = event.getValue();

        if(historyToken.equals("history")) {
          switchPanel.showWidget(1);
        } else {
          switchPanel.showWidget(0);
        }
      }
    });

    History.fireCurrentHistoryState();
  }

  @UiHandler("vendorButton")
  public void onVendorClickHandler(final ClickEvent e) {
    History.newItem("");
  }

  @UiHandler("historyButton")
  public void onHistoryClickHandler(final ClickEvent e) {
    History.newItem("history");
  }
}
