package co.fxl.gui.api;

public interface IFontElement {
    IFont font();

    public interface IFont extends IColored {
        IFamily family();

        IWeight weight();

        IFont pixel(int pixel);

        IFont underline(boolean underline);

        public interface IFamily {
            IFont arial();

            IFont timesNewRoman();

            IFont verdana();

            IFont lucinda();

            IFont helvetica();

            IFont courier();

            IFont georgia();

            IFont name(String font);

            IFont garamond();

            IFont calibri();
        }

        public interface IWeight {
            IFont bold();

            IFont italic();

            IFont plain();
        }
    }
}
