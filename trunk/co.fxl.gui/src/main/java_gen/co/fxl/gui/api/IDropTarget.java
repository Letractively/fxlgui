package co.fxl.gui.api;

public interface IDropTarget<T> {
    T addDragOverListener(IDragMoveListener l);

    T addDropListener(IDropListener l);

    public interface IDragEvent extends IPoint {
        String iD();

        boolean shift();

        boolean ctrl();
    }

    public interface IDragMoveListener {
        void onDragOver(IDragEvent point);

        void onDragOut(IDragEvent point);
    }

    public interface IDropListener {
        void onDropOn(IDragEvent point);
    }
}
