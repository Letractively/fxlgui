package co.fxl.gui.layout.api;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPasswordField;
import co.fxl.gui.api.ITextField;


public interface ILoginLayout {
    ILoginLayout id(ITextField id);

    ILoginLayout password(IPasswordField password);

    ILoginLayout panel(IHorizontalPanel panel);

    ILoginLayout label(ILabel loginLabel);

    ILoginLayout loginListener(IClickable.IClickListener l);

    ILoginLayout loggedInAs(ILabel label);

    ILoginLayout loginPanel(IHorizontalPanel p);
}
