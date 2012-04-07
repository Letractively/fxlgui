package co.fxl.gui.layout.api;

import java.util.List;


public interface INavigationGroupLayout {
    INavigationGroupLayout visible(boolean visible);

    String name();

    List<co.fxl.gui.layout.api.INavigationItemLayout> items();
}
