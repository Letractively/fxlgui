package co.fxl.gui.mdt.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.style.IStyle;
import co.fxl.gui.style.Styles;

public class Style {

	public enum MDT {
		VIEW;
	}

	public static void setUp() {
		Styles.instance().register(new IStyle<ILabel>() {
			@Override
			public void style(ILabel label) {
				if (!label.clickable()) {
					label.font().weight().bold().color().black();
				} else {
					label.font().weight().plain();
				}
			}
		}, MDT.VIEW);
	}
}
