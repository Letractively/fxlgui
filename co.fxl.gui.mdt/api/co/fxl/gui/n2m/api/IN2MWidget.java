package co.fxl.gui.n2m.api;

import java.util.List;

public interface IN2MWidget<T> {

	public interface IN2MRelationListener<T> {

		void onChange(List<T> selection);
	}

	IN2MWidget<T> domain(List<T> tokens);

	IN2MWidget<T> selection(List<T> tokens);

	List<T> selection();

	IN2MWidget<T> listener(IN2MRelationListener<T> listener);

}
