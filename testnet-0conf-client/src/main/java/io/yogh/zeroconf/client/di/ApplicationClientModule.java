package io.yogh.zeroconf.client.di;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import io.yogh.zeroconf.client.ui.VendorView;
import io.yogh.zeroconf.client.ui.VendorViewImpl;

public class ApplicationClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    bind(PlaceController.class).to(ApplicationPlaceController.class).in(Singleton.class);

    bind(VendorView.class).to(VendorViewImpl.class);
  }

  public static class ApplicationPlaceController extends PlaceController {
    @Inject
    public ApplicationPlaceController(final EventBus eventBus) {
      super(eventBus);
    }
  }
}
