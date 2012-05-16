package co.fxl.gui.layout.api;

import java.util.List;

public interface INavigationGroupLayout {

	INavigationGroupLayout visible(boolean visible);

	String name();

	List<INavigationItemLayout> items();

}