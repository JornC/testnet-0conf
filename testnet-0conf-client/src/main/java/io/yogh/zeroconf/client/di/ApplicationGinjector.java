package io.yogh.zeroconf.client.di;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import io.yogh.zeroconf.client.Application;
import io.yogh.zeroconf.client.ApplicationRootView;

@GinModules(value = ApplicationClientModule.class)
public interface ApplicationGinjector extends Ginjector {
  ApplicationGinjector INSTANCE = GWT.create(ApplicationGinjector.class);

  void inject(Application application);

  ApplicationRootView getApplicationRootView();
}
