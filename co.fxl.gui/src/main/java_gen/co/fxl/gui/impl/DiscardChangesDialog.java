package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IDialog;
import co.fxl.gui.api.IDisplay;


public class DiscardChangesDialog {
    public static final String DISCARD_CHANGES = "You have made changes that have not been saved!\nDiscard Changes?";
    private static boolean active = false;
    public static IDisplay display;
    public static DiscardChangesListener listener;

    public static void show(final ICallback<java.lang.Boolean> callback) {
        if (!(DiscardChangesDialog.active)) {
            callback.onSuccess(true);
        } else {
            IDialog dl = DiscardChangesDialog.display.showDialog().confirm()
                                                     .message(DISCARD_CHANGES)
                                                     .warn();
            dl.addButton().yes().addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        DiscardChangesDialog.active = false;

                        if ((DiscardChangesDialog.listener) != null) {
                            DiscardChangesDialog.listener.onDiscardChanges(new CallbackTemplate<java.lang.Boolean>(
                                    callback) {
                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            DiscardChangesDialog.listener = null;
                                        }

                                        callback.onSuccess(result);
                                    }
                                });
                        } else {
                            callback.onSuccess(true);
                        }
                    }
                });
            dl.addButton().no().addClickListener(new IClickable.IClickListener() {
                    @Override
                    public void onClick() {
                        if ((DiscardChangesDialog.listener) != null) {
                            DiscardChangesDialog.listener.onKeepChanges(new CallbackTemplate<java.lang.Boolean>(
                                    callback) {
                                    @Override
                                    public void onSuccess(Boolean result) {
                                        if (result) {
                                            DiscardChangesDialog.listener = null;
                                        }

                                        callback.onSuccess(false);
                                    }
                                });
                        } else {
                            callback.onSuccess(false);
                        }
                    }
                });
            dl.visible(true);
        }
    }

    public static void active(boolean active) {
        DiscardChangesDialog.active = active;
    }

    public interface DiscardChangesListener {
        void onDiscardChanges(ICallback<java.lang.Boolean> cb);

        void onKeepChanges(ICallback<java.lang.Boolean> cb);
    }
}
