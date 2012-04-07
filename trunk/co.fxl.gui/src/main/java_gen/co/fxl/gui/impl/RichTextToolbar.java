package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPopUp;
import co.fxl.gui.api.IRichTextArea;
import co.fxl.gui.api.IToggleButton;
import co.fxl.gui.api.IUpdateable;


public class RichTextToolbar {
    public RichTextToolbar(final IRichTextArea rta, final IRichTextAdapter adp) {
        rta.addFocusListener(new IUpdateable.IUpdateListener<java.lang.Boolean>() {
                private IPopUp popUp;

                @Override
                public void onUpdate(Boolean value) {
                    if (((popUp) == null) && value) {
                        popUp = rta.display().showPopUp();
                        popUp.offset(rta.offsetX(),
                            ((rta.offsetY()) + (rta.height())));
                        popUp.size(rta.width(), 40);

                        IHorizontalPanel h = popUp.container().panel()
                                                  .horizontal().spacing(4);
                        h.color().lightgray();
                        h.border().color().gray();
                        h.width(rta.width());
                        h.add().toggleButton().text("b").down(adp.isBold()).addUpdateListener(new IUpdateable.IUpdateListener<java.lang.Boolean>() {
                                @Override
                                public void onUpdate(Boolean value) {
                                    if ((adp.isBold()) != value) {
                                        adp.toggleBold();
                                    }

                                    rta.focus(true);
                                }
                            });
                        popUp.height(h.height());
                        popUp.visible(true);
                    } else if (((popUp) != null) && (!value)) {
                        popUp.visible(false);
                        popUp = null;
                    }
                }
            });
    }

    public interface IRichTextAdapter {
        void toggleBold();

        boolean isBold();
    }
}
