package co.fxl.gui.impl;

import co.fxl.gui.api.IBordered;
import co.fxl.gui.api.IBordered.IBorder;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IColored;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.api.IFontElement;
import co.fxl.gui.api.IFontElement.IFont;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.ITextArea;
import co.fxl.gui.api.IVerticalPanel;


public class ErrorDialog {
    private static final int DEFAULT_WIDTH = 420;
    private static boolean showing = false;
    private Runnable runnable;

    private ErrorDialog() {
    }

    public static void create(String pTitle, String pMessage,
        String pStacktrace, Runnable pRunnable) {
        if (ErrorDialog.showing) {
            return;
        }

        ErrorDialog lErrorDialog = new ErrorDialog();
        lErrorDialog.runnable = pRunnable;
        lErrorDialog.show(pTitle, pMessage, pStacktrace);
    }

    public static void create(String pTitle, String pMessage, String pStacktrace) {
        ErrorDialog.create(pTitle, pMessage, pStacktrace, null);
    }

    public void show(String pTitle, final String pMessage,
        final String pStacktrace) {
        ErrorDialog.showing = true;

        IDialog dialog = co.fxl.gui.impl.Display.instance().showDialog();
        dialog.width(DEFAULT_WIDTH);
        dialog.title(pTitle).message(pMessage).error();

        if (pStacktrace != null) {
            dialog.addButton().text("Details").imageResource(Icons.DETAIL).addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        IDialog detailDialog = co.fxl.gui.impl.Display.instance()
                                                                      .showDialog();
                        detailDialog.width(DEFAULT_WIDTH);
                        detailDialog.title("Error Details");
                        detailDialog.addButton().close().addClickListener(new IClickable.IClickListener() {
                                @Override
                                public void onClick() {
                                    onClose();
                                }
                            });

                        IContainer container = detailDialog.container();
                        IVerticalPanel detailPanel = container.panel().vertical();
                        IVerticalPanel panel = detailPanel.spacing(10).add()
                                                          .panel().vertical();
                        ILabel label = panel.add().label().text("Stacktrace:");
                        styleDialogError(label);

                        ITextArea textArea = panel.add().textArea()
                                                  .size(400, 100)
                                                  .text(pStacktrace);
                        styleInputBorder(textArea);
                        detailDialog.visible(true);
                    }

                    private void styleInputBorder(ITextArea textArea) {
                        textArea.border().color().rgb(211, 211, 211);
                    }

                    private void styleDialogError(ILabel label) {
                        label.font().weight().italic();
                    }
                });
        }

        dialog.addButton().close().imageResource(Icons.CANCEL).addClickListener(new IClickable.IClickListener() {
                @Override
                public void onClick() {
                    onClose();
                }
            });
        dialog.visible(true);
    }

    protected void onClose() {
        ErrorDialog.showing = false;

        if ((runnable) != null) {
            runnable.run();
        }
    }
}
