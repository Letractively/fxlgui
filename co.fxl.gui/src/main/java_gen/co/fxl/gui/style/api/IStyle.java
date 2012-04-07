package co.fxl.gui.style.api;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILinearPanel;
import co.fxl.gui.api.IPanel;


public interface IStyle {
    ITop top();

    IOptionMenu optionMenu();

    ITable table();

    ITree tree();

    INavigation navigation();

    IRegister register();

    IWindow window();

    ILogin login();

    IStyle background(IPanel<?> panel);

    IStyle hyperlink(ILabel label);

    IStyle side(ILinearPanel<?> panel);

    public interface ITop {
        String imageResource();

        ITop panel(IPanel<?> panel);

        int spacing();
    }

    public interface IOptionMenu {
        ILabel addCommand(IPanel<?> panel, String text);

        IOptionMenu label(ILabel label);

        IOptionMenu searchButton(IHorizontalPanel buttonPanel);
    }

    public interface ITree {
        ITree panel(IPanel<?> panel);
    }

    public interface ITable {
        ITable statusPanel(IPanel<?> panel);

        IColumnSelection columnSelection();

        ITable topPanel(IPanel<?> topPanel);

        public interface IColumnSelection {
            IColumnSelection panel(IPanel<?> panel, boolean visible);

            IColumnSelection label(ILabel label, boolean visible);
        }
    }

    public interface IRegister {
        IRegister cardPanel(IPanel<?> panel);

        IRegister topPanel(IPanel<?> panel);
    }

    public interface ILogin {
        ILogin label(ILabel text);

        ILogin hyperlink(ILabel text);
    }

    public interface IWindow {
        IWindow main(IPanel<?> panel, boolean addBorder);

        IWindow header(IPanel<?> panel, boolean isSide);

        IWindow footer(IPanel<?> panel);

        IWindow title(ILabel label, String title, boolean isSideWidget);

        IWindow navigationEntry(ILinearPanel<?> panel);

        IWindow button(IPanel<?> panel, boolean isSideWidget);

        ILabel addCommandLabel(ILinearPanel<?> panel, String text,
            boolean isSideWidget);

        boolean commandsOnTop();

        String moreImage();
    }

    public interface INavigation {
        INavigationGroup group();

        public interface INavigationGroup {
            INavigation groupPanel(ILinearPanel<?> panel);

            INavigationItem item();

            INavigation mainPanel(IPanel<?> panel);

            public interface INavigationItem {
                INavigation active(ILinearPanel<?> panel, ILabel label);

                INavigation inactive(ILinearPanel<?> panel, ILabel label);

                String image(String resource);
            }
        }
    }
}
