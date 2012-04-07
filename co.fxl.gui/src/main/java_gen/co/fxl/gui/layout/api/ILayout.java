package co.fxl.gui.layout.api;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;


public interface ILayout {
    INavigationLayout navigationMain();

    INavigationLayout navigationSub();

    IFormLayout form();

    ILoginLayout login();

    IActionMenuLayout actionMenu();

    ILabel createWindowButton(boolean commandsOnTop, IHorizontalPanel panel,
        String text);

    ILinearPanel<?> createLinearPanel(IContainer c);

    ILinearPanel<?> createLinearPanel(IContainer c, boolean isForFilterQuery);

    IDialogLayout dialog(DialogType type);

    ITreeLayout tree();

    IMDTLayout mdt();

    IImage logo(IImage image);
}
