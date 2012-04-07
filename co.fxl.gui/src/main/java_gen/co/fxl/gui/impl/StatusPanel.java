package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IPopUp;


public class StatusPanel {
    private static IPopUp lastPopUp;
    private static String lastStatus;

    public static void start(String status) {
        StatusPanel.lastStatus = status;
        StatusPanel.lastPopUp = StatusPanel.showPopUp(co.fxl.gui.impl.Display.instance(),
                ("Loading " + status), true, 0);
    }

    public static void stop(String status) {
        if (StatusPanel.lastStatus.equals(status)) {
            StatusPanel.lastPopUp.visible(false);
            StatusPanel.lastPopUp = null;
        }
    }

    public static void stop() {
        StatusPanel.stop(StatusPanel.lastStatus);
    }

    public static IPopUp showPopUp(IDisplay display, String info,
        boolean modal, int y) {
        IPopUp dialog = display.showPopUp().modal(true).glass(false);
        dialog.border().remove();

        IBorder b = dialog.border();
        b.style().shadow();

        IHorizontalPanel spacing = dialog.container().panel().horizontal()
                                         .spacing(5);
        spacing.color().rgb(255, 240, 170);
        spacing.addSpace(4).add().label()
               .text((("Please wait - " + info) + "...")).font().pixel(11);
        spacing.addSpace(4);

        int x = ((display.width()) - (dialog.width())) / 2;
        dialog.offset(x, 4);
        dialog.visible(true);
        x = ((display.width()) - (dialog.width())) / 2;
        dialog.offset(x, 4);

        return dialog;
    }
}
