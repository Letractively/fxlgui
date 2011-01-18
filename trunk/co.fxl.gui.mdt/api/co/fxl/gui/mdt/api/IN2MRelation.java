package co.fxl.gui.mdt.api;

import java.util.List;

import co.fxl.gui.api.template.ICallback;

public interface IN2MRelation<T, R> {

	public interface IAdapter<T, R> {

		void valueOf(T entity, ICallback<List<R>> callback);

		void valueOf(T entity, List<R> values, ICallback<List<R>> callback);
	}

	IN2MRelation<T, R> domain(List<R> domain);

	IN2MRelation<T, R> adapter(IAdapter<T, R> adapter);
	
}
