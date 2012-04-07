package co.fxl.gui.impl;

import co.fxl.gui.api.IAlignment;
import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IVerticalPanel;

import java.util.LinkedList;
import java.util.List;


public class DialogImpl implements IDialog {
    private IDisplay display;
    private boolean modal = true;
    private String title = "Dialog";
    protected String message;
    protected String type = "Information";
    private IPopUp popUp;
    private List<co.fxl.gui.impl.DialogImpl.DialogButtonImpl> buttons = new LinkedList<co.fxl.gui.impl.DialogImpl.DialogButtonImpl>();
    private IContainer container;
    protected int width = -1;
    private int height = -1;

    public DialogImpl(IDisplay display) {
        this.display = display;
        confirm();
    }

    @Override
    public IDialog modal(boolean modal) {
        this.modal = modal;

        return this;
    }

    @Override
    public IDialog title(String title) {
        this.title = title;

        return this;
    }

    @Override
    public IDialog.IType message(String message) {
        this.message = message;

        return new IDialog.IType() {
                private IDialog type(String string) {
                    type = string;

                    if ((title) == null) {
                        title(string);
                    }

                    return DialogImpl.this;
                }

                @Override
                public IDialog error() {
                    return type("Error");
                }

                @Override
                public IDialog info() {
                    return type("Information");
                }

                @Override
                public IDialog warn() {
                    return type("Warning");
                }
            };
    }

    @Override
    public IContainer container() {
        if ((container) == null) {
            if (((title) != null) || (!(buttons.isEmpty()))) {
                getPopUp();
            } else {
                popUp = display.showPopUp().modal(modal);
                decorateBorder();
                container = popUp.container();
            }
        }

        return container;
    }

    IPopUp getPopUp() {
        if ((popUp) == null) {
            popUp = display.showPopUp().modal(modal).autoHide(false);
            decorateBorder();

            if (((width) != (-1)) && ((height) != (-1))) {
                popUp.size(width, height);
            } else if ((width) != (-1)) {
                popUp.width(width);
            } else if ((height) != (-1)) {
                popUp.height(height);
            }

            popUp.center();

            IVerticalPanel panel = popUp.container().panel().vertical();
            WidgetTitle.decorateBorder(panel.spacing(1).border().color());

            WidgetTitle t = new WidgetTitle(panel.add().panel()).foldable(false)
                                                                .space(0);
            t.addTitle(title.toUpperCase());
            t.addTitleSpace();

            if (((message) != null) && (buttons.isEmpty())) {
                addButton().ok().addClickListener(new IClickable.IClickListener() {
                        @Override
                        public void onClick() {
                        }
                    });
            }

            for (DialogButtonImpl b : buttons) {
                CommandLink l = t.addHyperlink(b.imageResource, b.text);

                for (IClickable.IClickListener cl : b.listeners)
                    l.addClickListener(cl);

                b.link(l);
            }

            IContainer content = t.content();

            if ((message) != null) {
                createLabel(content);
                decorate(popUp, panel);
            } else {
                container = content;
            }

            if ((panel.width()) < 200) {
                panel.width(200);
            }

            if ((((content != null) && ((content.element()) != null)) &&
                    ((content.element().nativeElement()) != null)) &&
                    ((content.element().height()) < 40)) {
                content.element().height(40);
            }

            t.adjustHeader();
        }

        return popUp;
    }

    void decorateBorder() {
        popUp.border().remove();
        popUp.border().width(1);
        popUp.border().style().shadow();
    }

    protected void createLabel(IContainer content) {
        IGridPanel grid = content.panel().vertical().add().panel().grid()
                                 .spacing(10).resize(2, 1);

        if ((width) != (-1)) {
            decorate(grid);
        }

        grid.cell(0, 0).width(16).align().begin().valign().begin().image()
            .resource(image(type)).size(16, 16);

        IGridPanel.IGridCell c = grid.cell(1, 0).align().begin().valign()
                                     .center();

        if ((width) != (-1)) {
            c.width((((width) - (3 * 10)) - 16));
        }

        c.label().text(message).autoWrap(true);
    }

    protected void decorate(IPopUp popUp, IVerticalPanel panel) {
    }

    protected void decorate(IGridPanel grid) {
    }

    @Override
    public IDialog visible(boolean visible) {
        getPopUp().visible(visible);

        return this;
    }

    protected String image(String type2) {
        if (type.equals("Information")) {
            return Icons.INFO;
        } else if (type.equals("Warning")) {
            return Icons.SKIP;
        } else if (type.equals("Error")) {
            return Icons.CANCEL;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public IDialog.IDialogButton addButton() {
        DialogButtonImpl button = new DialogButtonImpl();
        buttons.add(button);

        return button;
    }

    @Override
    public IDialog confirm() {
        return title("Please Confirm");
    }

    @Override
    public IDialog size(int width, int height) {
        this.width = width;
        this.height = height;

        return this;
    }

    @Override
    public IDialog width(int width) {
        this.width = width;

        return this;
    }

    @Override
    public IDialog height(int height) {
        this.height = height;

        return this;
    }

    private class DialogButtonImpl implements IDialog.IDialogButton {
        private List<co.fxl.gui.api.IClickable.IClickListener> listeners = new LinkedList<co.fxl.gui.api.IClickable.IClickListener>();
        private String imageResource;
        private String text;
        private CommandLink l;

        DialogButtonImpl() {
            listeners.add(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        DialogImpl.this.visible(false);
                    }
                });
        }

        @Override
        public IDialog.IDialogButton imageResource(String imageResource) {
            this.imageResource = imageResource;

            return this;
        }

        @Override
        public IDialog.IDialogButton text(String text) {
            this.text = text;

            return this;
        }

        @Override
        public IDialog.IDialogButton ok() {
            return imageResource(Icons.ACCEPT).text("Ok");
        }

        @Override
        public IDialog.IDialogButton close() {
            return imageResource(Icons.CANCEL).text("Close");
        }

        @Override
        public IDialog.IDialogButton yes() {
            return imageResource(Icons.ACCEPT).text("Yes");
        }

        @Override
        public IDialog.IDialogButton no() {
            return imageResource(Icons.CANCEL).text("No");
        }

        @Override
        public IDialog.IDialogButton cancel() {
            return imageResource(Icons.CANCEL).text("Cancel");
        }

        @Override
        public IDialog.IDialogButton addClickListener(
            final IClickable.IClickListener l) {
            listeners.add(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        l.onClick();
                    }
                });

            return this;
        }

        @Override
        public IDialog.IDialogButton clickable(boolean clickable) {
            if ((l) != null) {
                l.clickable(clickable);
            }

            return this;
        }

        public void link(CommandLink l) {
            this.l = l;
        }
    }
}
