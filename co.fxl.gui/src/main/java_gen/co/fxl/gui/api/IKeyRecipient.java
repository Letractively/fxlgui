package co.fxl.gui.api;

public interface IKeyRecipient<T> {
    IKey<T> addKeyListener(IClickable.IClickListener listener);

    public interface IKey<T> {
        IKey<T> ctrl();

        T enter();

        T tab();

        T up();

        T down();

        T left();

        T right();

        T character(char c);
    }
}
