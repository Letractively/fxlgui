package co.fxl.gui.layout.api;

import co.fxl.gui.api.IPanel;

import java.util.List;


public interface INavigationLayout {
    INavigationLayout groups(
        List<co.fxl.gui.layout.api.INavigationGroupLayout> groups);

    INavigationLayout group(INavigationGroupLayout group);

    INavigationLayout panel(IPanel<?> panel);

    INavigationLayout visible(INavigationItemLayout item, boolean visible);

    INavigationLayout active(INavigationItemLayout item, boolean active);
}
