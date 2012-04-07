package co.fxl.gui.api;

public interface IBordered {
    IBorder border();

    public interface IBorder extends IColored {
        IBorderStyle style();

        IBorder width(int width);

        IBorder title(String title);

        void remove();

        public interface IBorderStyle {
            IBorder shadow();

            IBorder dotted();

            IBorder rounded();

            IBorder solid();

            IBorder etched();

            IBorder top();

            IBorder bottom();

            IBorder noBottom();

            IBorder right();

            IBorder left();
        }
    }
}
