package co.fxl.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;

class PanelComponent extends JPanel {

	private static final long serialVersionUID = 4786977574399962374L;

	int preferredWidth = -1;
	int preferredHeight = -1;

	SwingPanel<?>.SwingPanelColor fxlColor;

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

	@Override
	public void paintComponent(Graphics g) {
		if (fxlColor == null || fxlColor.gradient == null) {
			super.paintComponent(g);
			return;
		}
		int width = getWidth();
		int height = getHeight();
		GradientPaint paint = null;
		Color sc = getBackground();
		Color ec = new Color(fxlColor.gradient.color.rgb[0],
				fxlColor.gradient.color.rgb[1], fxlColor.gradient.color.rgb[2]);
		paint = new GradientPaint(width / 2, 0, sc, width / 2, height, ec);
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(paint);
		g2d.fillRect(0, 0, width, height);
		g2d.setPaint(oldPaint);
		// super.paintComponent(g);
	}
}
