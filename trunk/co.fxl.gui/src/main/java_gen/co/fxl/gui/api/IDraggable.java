package co.fxl.gui.api;

public interface IDraggable<T> {
    T draggable(boolean draggable);

    T addDragStartListener(IDragStartListener l);

    public interface IDragStartListener {
        void onDragStart(IDragStartEvent event);

        void onDragEnd();

        public interface IDragStartEvent extends IPoint {
            IDragStartEvent dragImage(IElement<?> element);

            IDragStartEvent iD(String iD);
        }
    }
}
