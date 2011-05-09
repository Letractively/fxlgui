package co.fxl.gui.mdt.api;

import java.util.List;

import co.fxl.gui.api.template.ICallback;
import co.fxl.gui.n2m.api.IN2MWidget.IItemImageProvider;

public interface IN2MRelation<T, R> {

	public interface IAdapter<T, R> {

		void domain(T entity, ICallback<List<R>> callback);

		void valueOf(T entity, ICallback<List<R>> callback);

		void valueOf(T entity, List<R> values, ICallback<List<R>> callback);

		boolean editable(T entity);
	}

	IN2MRelation<T, R> adapter(IAdapter<T, R> adapter);

	IN2MRelation<T, R> constrainType(Class<?> class1);

	IN2MRelation<T, R> itemImage(String itemImage);

	IN2MRelation<T, R> itemImageProvider(IItemImageProvider<R> itemImageProvider);

	IN2MRelation<T, R> allowReorder(boolean allowReorder);

}
