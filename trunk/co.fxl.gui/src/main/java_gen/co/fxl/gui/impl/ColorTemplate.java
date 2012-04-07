package co.fxl.gui.impl;

import co.fxl.gui.api.IColored;


public abstract class ColorTemplate implements IColored.IColor {
    public Gradient gradient;
    public int[] rgb;

    @Override
    public IColored.IColor black() {
        return rgb(0, 0, 0);
    }

    @Override
    public IColored.IColor rgb(int r, int g, int b) {
        rgb = new int[] { r, g, b };

        return update();
    }

    private IColored.IColor update() {
        return setRGB(rgb[0], rgb[1], rgb[2]);
    }

    protected abstract IColored.IColor setRGB(int r, int g, int b);

    @Override
    public IColored.IColor green() {
        return rgb(0, 255, 0);
    }

    @Override
    public IColored.IColor blue() {
        return rgb(0, 0, 255);
    }

    @Override
    public IColored.IColor gray() {
        return rgb(140, 140, 140);
    }

    @Override
    public IColored.IColor lightgray() {
        return rgb(211, 211, 211);
    }

    @Override
    public IColored.IColor red() {
        return rgb(255, 0, 0);
    }

    @Override
    public IColored.IColor white() {
        return rgb(255, 255, 255);
    }

    @Override
    public IColored.IColor yellow() {
        return rgb(255, 255, 0);
    }

    @Override
    public IColored.IColor mix() {
        return new MixColor();
    }

    @Override
    public IColored.IGradient gradient() {
        return gradient = new Gradient(this);
    }

    public class Gradient implements IColored.IGradient {
        public ColorTemplate color;
        public int[] fallback = null;

        Gradient(final ColorTemplate original) {
            color = new ColorTemplate() {
                        @Override
                        public IColored.IColor remove() {
                            rgb = null;

                            return this;
                        }

                        @Override
                        protected IColored.IColor setRGB(int r, int g, int b) {
                            original.update();

                            return this;
                        }
                    };
        }

        @Override
        public IColored.IColor vertical() {
            return color;
        }

        @Override
        public IColored.IGradient fallback(int r, int g, int b) {
            fallback = new int[] { r, g, b };

            return this;
        }
    }

    private class MixColor extends ColorTemplate implements IColored.IColor {
        private int r = 0;
        private int g = 0;
        private int b = 0;
        private int nCalls = 0;

        @Override
        public IColored.IColor setRGB(int r, int g, int b) {
            this.r += r;
            this.g += g;
            this.b += b;
            (nCalls)++;

            int newR = (this.r) / (nCalls);
            int newG = (this.g) / (nCalls);
            int newB = (this.b) / (nCalls);
            ColorTemplate.this.rgb(newR, newG, newB);

            return this;
        }

        @Override
        public IColored.IColor remove() {
            return ColorTemplate.this.remove();
        }
    }
}
