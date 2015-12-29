package io.yogh.zeroconf.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import io.yogh.zeroconf.client.di.ApplicationGinjector;
import io.yogh.zeroconf.client.resources.R;

public class Application implements EntryPoint {
  @Override
  public void onModuleLoad() {
    ApplicationGinjector.INSTANCE.inject(this);

    R.init();

    RootPanel.get().add(ApplicationGinjector.INSTANCE.getApplicationRootView());
  }
}
