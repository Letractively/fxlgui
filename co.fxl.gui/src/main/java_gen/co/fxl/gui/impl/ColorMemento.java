package co.fxl.gui.impl;

import co.fxl.gui.api.IColored;


public class ColorMemento extends ColorTemplate {
    @Override
    public IColored.IColor remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected IColored.IColor setRGB(int r, int g, int b) {
        return this;
    }

    public void forward(IColored.IColor color) {
        color.rgb(rgb[0], rgb[1], rgb[2]);
    }
}
