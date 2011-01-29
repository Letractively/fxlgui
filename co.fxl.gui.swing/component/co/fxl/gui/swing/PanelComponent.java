package co.fxl.gui.swing;

import java.awt.Dimension;

import javax.swing.JPanel;

class PanelComponent extends JPanel {

	private static final long serialVersionUID = 4786977574399962374L;

	int preferredWidth = -1;
	int preferredHeight = -1;

	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		if (preferredWidth != -1) {
			d.width = preferredWidth;
		}
		if (preferredHeight != -1) {
			d.height = preferredHeight;
		}
		return d;
	}
}
