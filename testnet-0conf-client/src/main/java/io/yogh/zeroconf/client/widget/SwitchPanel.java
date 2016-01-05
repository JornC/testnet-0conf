/*
 * Copyright Dutch Ministry of Economic Affairs, Agriculture and Innovation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package io.yogh.zeroconf.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class SwitchPanel extends FlowPanel {
  private Widget activeWidget;

  public void showWidget(final int index) {
    final Widget showWidget = getWidget(index);

    if (showWidget == null || showWidget == activeWidget) {
      return;
    }

    if (activeWidget != null) {
      setWidgetVisible(activeWidget, false);
    }

    activeWidget = showWidget;

    setWidgetVisible(activeWidget, true);
  }

  private void setWidgetVisible(final Widget widget, final boolean visible) {
    widget.setVisible(visible);
  }

  @Override
  public void add(final Widget w) {
    w.setVisible(false);

    super.add(w);
  }
}