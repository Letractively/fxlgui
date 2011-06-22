package co.fxl.gui.tree.test;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;
import co.fxl.gui.tree.api.ILazyTreeWidget;
import co.fxl.gui.tree.api.ILazyTreeWidget.ILazyTreeListener;

public class LazyTreeWidgetTest implements ILazyTreeListener {

	private ILazyTreeWidget tree;

	public LazyTreeWidgetTest(IDisplay display) {
		display.register(new LazyScrollPanelImplWidgetProvider());
		IVerticalPanel panel = display.container().panel().vertical();
		tree = (ILazyTreeWidget) panel.add().widget(ILazyTreeWidget.class);
		tree.tree(new TestLazyTree(5));
		tree.height(600);
		tree.addListener(this);
		tree.visible(true);
		display.visible(true);
	}

	@Override
	public void onClick(int index) {
		tree.elementAt(index).label().text("Hell");
	}
}
