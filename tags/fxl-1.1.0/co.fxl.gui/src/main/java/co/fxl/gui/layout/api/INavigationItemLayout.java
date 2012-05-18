package co.fxl.gui.layout.api;

public interface INavigationItemLayout {

	String name();

	INavigationItemLayout updateActive();

	boolean isActive();

	INavigationItemLayout updateVisible(boolean visible);

}