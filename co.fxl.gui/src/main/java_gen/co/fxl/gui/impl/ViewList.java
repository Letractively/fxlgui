package co.fxl.gui.impl;

import co.fxl.gui.api.ICallback;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;

import java.util.LinkedList;
import java.util.List;


public class ViewList {
    MetaViewList widget;
    WidgetTitle widgetTitle;
    IVerticalPanel panel;
    List<co.fxl.gui.impl.ViewImpl> views = new LinkedList<co.fxl.gui.impl.ViewImpl>();
    NewListener newListener = null;
    boolean hasLinks;

    public ViewList(MetaViewList widget, ILayout layout) {
        this.widget = widget;
        widgetTitle = new WidgetTitle(layout, true).sideWidget(true).space(2);
    }

    public ViewImpl addView() {
        return addView(null);
    }

    public ViewImpl addView(String imageResource) {
        return addView(imageResource, false);
    }

    public ViewImpl addView(String imageResource, boolean isNew) {
        if ((widgetTitle.headerLabel) == null) {
            title("Views");
        }

        if ((panel) == null) {
            final IGridPanel grid = widgetTitle.content().panel().grid();
            grid.cell(0, 0).width(4).label();

            IVerticalPanel vertical = grid.cell(1, 0).panel().vertical();
            grid.cell(2, 0).width(4).label();
            vertical.addSpace(2);
            panel = vertical.add().panel().vertical().spacing(2);
            vertical.addSpace(4);
        }

        ViewImpl view = new ViewImpl(this, imageResource, isNew);
        views.add(view);

        return view;
    }

    public ViewList modifiable(String icon, final String typeIcon, String name,
        NewListener listener) {
        newListener = listener;
        widgetTitle.addHyperlink(icon,
            ("New" + ((name == null) ? "" : (" " + name)))).addClickListener(new LazyClickListener() {
                @Override
                public void onAllowedClick() {
                    addView(typeIcon, true);
                }
            });

        return this;
    }

    public ViewList title(String title) {
        widgetTitle.addTitle(title);

        return this;
    }

    public ViewList foldable(boolean foldable) {
        widgetTitle.foldable(foldable);

        return this;
    }

    protected void remove(final IGridPanel grid, final ViewImpl view,
        final ICallback<java.lang.Boolean> remove) {
        if ((view.title()) != null) {
            newListener.onRemove(view,
                new CallbackTemplate<java.lang.Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            removeView(grid, view);
                        }

                        remove.onSuccess(result);
                    }
                });

        } else {
            removeView(grid, view);
        }
    }

    protected void removeView(IGridPanel grid, ViewImpl view) {
        grid.remove();

        int i = views.indexOf(view);
        views.remove(view);

        if (i < (views.size())) {
            views.get(i).onClick();
        } else if (!(views.isEmpty())) {
            views.get(((views.size()) - 1)).onClick();
        } else {
            widget.selectFirst();
        }
    }

    public void showFirstVisible(ICallback<java.lang.Void> cb) {
        for (ViewImpl view : views) {
            if (view.visible()) {
                view.onClick(cb);

                return;
            }
        }

        cb.onSuccess(null);
    }

    public CommandLink addHyperlink(String imageResource, String text) {
        return widgetTitle.addHyperlink(imageResource, text);
    }

    public interface NewListener {
        void onNew(ViewImpl view, ICallback<java.lang.Void> cb);

        boolean isRemovable(ViewImpl view);

        void onRemove(ViewImpl view, ICallback<java.lang.Boolean> remove);
    }
}
