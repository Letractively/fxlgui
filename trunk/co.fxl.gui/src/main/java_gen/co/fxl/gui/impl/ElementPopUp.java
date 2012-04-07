package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IElement;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IMouseOverElement;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IScrollPane;
import co.fxl.gui.api.IVerticalPanel;


public class ElementPopUp {
    public static Heights HEIGHTS = new Heights(0);
    private IPopUp popUp;
    private IElement<?> element;
    private int lines = -1;
    private ColorMemento color;
    private boolean scrollPane = true;
    private int rowHeight = 19;

    public ElementPopUp(IElement<?> element) {
        this.element = element;
    }

    public ElementPopUp lines(int lines) {
        this.lines = lines;

        return this;
    }

    public IColored.IColor color() {
        return color = new ColorMemento();
    }

    public ElementPopUp scrollPane(boolean scrollPane) {
        this.scrollPane = scrollPane;

        return this;
    }

    public ElementPopUp rowHeight(int rowHeight) {
        this.rowHeight = rowHeight;

        return this;
    }

    public IVerticalPanel create() {
        popUp = element.display().showPopUp().autoHide(true);
        ElementPopUp.HEIGHTS.decorateBorder(popUp).style().shadow();

        int w = java.lang.Math.min(320, element.width());
        int h = 0;

        if ((lines) >= 1) {
            h = 4 + ((rowHeight) * (lines));
        }

        if (h > 0) {
            popUp.size(w, h);
        } else {
            popUp.width(w);
        }

        popUp.offset(element.offsetX(),
            ((element.offsetY()) + (element.height())));

        IContainer container = popUp.container();

        if (scrollPane) {
            IScrollPane scrollPane = container.scrollPane().width(w);
            scrollPane.color().white();

            if ((color) != null) {
                color.forward(scrollPane.color());
            }

            scrollPane.border().remove();
            container = scrollPane.viewPort();
        }

        IVerticalPanel panel = container.panel().vertical().spacing(4);

        if ((color) != null) {
            color.forward(panel.color());
        }

        return panel;
    }

    public ElementPopUp clear() {
        if ((popUp) != null) {
            popUp.visible(false);
            popUp = null;
        }

        return this;
    }

    public ElementPopUp visible(boolean b) {
        popUp.visible(b);

        return this;
    }

    public ElementPopUp onMouseOver(final Decorator decorator) {
        if ((element) instanceof IMouseOverElement) {
            IMouseOverElement<?> moe = ((IMouseOverElement<?>) (element));
            moe.addMouseOverListener(new IMouseOverElement.IMouseOverListener() {
                    @Override
                    public void onMouseOver() {
                        IVerticalPanel p = create();
                        decorator.decorate(p);
                        visible(true);
                    }

                    @Override
                    public void onMouseOut() {
                        clear();
                    }
                });
        }

        return this;
    }

    public ElementPopUp onRightClick(final Decorator decorator) {
        if ((element) instanceof IClickable) {
            IClickable<?> c = ((IClickable<?>) (element));
            c.addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        IVerticalPanel p = create();
                        decorator.decorate(p);
                        visible(true);
                    }
                }).mouseRight();
        }

        return this;
    }

    public interface Decorator {
        void decorate(IVerticalPanel panel);
    }
}
