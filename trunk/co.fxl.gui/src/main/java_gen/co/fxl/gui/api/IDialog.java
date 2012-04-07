package co.fxl.gui.api;

public interface IDialog {
    IDialog modal(boolean modal);

    IDialog title(String title);

    IType message(String message);

    IDialogButton addButton();

    IContainer container();

    IDialog visible(boolean visible);

    IDialog confirm();

    IDialog size(int width, int height);

    IDialog width(int width);

    IDialog height(int height);

    public interface IDialogButton {
        IDialogButton imageResource(String imageResource);

        IDialogButton text(String text);

        IDialogButton ok();

        IDialogButton yes();

        IDialogButton no();

        IDialogButton cancel();

        IDialogButton addClickListener(IClickable.IClickListener l);

        IDialogButton close();

        IDialogButton clickable(boolean clickable);
    }

    public interface IType {
        IDialog info();

        IDialog warn();

        IDialog error();
    }
}
