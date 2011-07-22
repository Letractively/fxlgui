package co.fxl.gui.tree.test;

import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.navigation.impl.NavigationWidgetImplProvider;
import co.fxl.gui.table.util.impl.LazyScrollPanelImplWidgetProvider;
import co.fxl.gui.tree.api.IFilterTreeWidget;
import co.fxl.gui.tree.api.ITree;
import co.fxl.gui.tree.api.ITreeWidget.IDecorator;
import co.fxl.gui.tree.impl.FilterTreeWidgetImplProvider;

public class FilterTreeWidgetTest implements IDecorator<Object> {

	private IFilterTreeWidget<Object> tree;

	@SuppressWarnings("unchecked")
	public FilterTreeWidgetTest(IDisplay display) {
		display.register(new FilterTreeWidgetImplProvider());
		display.register(new NavigationWidgetImplProvider());
		display.register(new LazyScrollPanelImplWidgetProvider());
		IVerticalPanel panel = display.container().panel().vertical();
		panel.height(600);
		tree = (IFilterTreeWidget<Object>) panel.add().widget(
				IFilterTreeWidget.class);
		tree.addDetailView("Details", this);
		tree.root(new TestLazyTree(5));
		display.fullscreen().visible(true);
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			ITree<Object> tree) {
		panel.clear().add().label().text(tree.name());
	}

	@Override
	public void decorate(IVerticalPanel panel, IVerticalPanel bottom,
			Object tree) {
	}

	@Override
	public boolean clear(IVerticalPanel contentPanel) {
		contentPanel.clear();
		return false;
	}
}
