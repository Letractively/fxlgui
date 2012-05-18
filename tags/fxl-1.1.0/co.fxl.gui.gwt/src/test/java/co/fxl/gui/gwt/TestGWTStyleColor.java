package co.fxl.gui.gwt;

public class TestGWTStyleColor {

	public static void main(String[] args) {
		String string = GWTStyleColor.toString(240, 240, 240);
		int[] rgb = GWTStyleColor.rgb(string);
		assert rgb[0] == 240 : string + ": " + rgb[0] + "!=" + 240;
		assert rgb[1] == 240;
		assert rgb[2] == 240;
	}
}
