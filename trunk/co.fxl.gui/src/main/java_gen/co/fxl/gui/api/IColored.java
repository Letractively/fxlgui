package co.fxl.gui.api;

public interface IColored {
    IColor color();

    public interface IGradient {
        IColor vertical();

        IGradient fallback(int r, int g, int b);
    }

    public interface IColor {
        IColor black();

        IColor gray();

        IColor lightgray();

        IColor white();

        IColor red();

        IColor green();

        IColor blue();

        IColor yellow();

        IColor rgb(int r, int g, int b);

        IColor mix();

        IGradient gradient();

        IColor remove();
    }
}
