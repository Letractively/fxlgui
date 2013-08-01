package co.fxl.gui.form.api;

import co.fxl.gui.api.ISuggestField;
import co.fxl.gui.filter.api.ISuggestionAdp;

public interface IRelationField extends IFormField<ISuggestField, String> {

	void suggestionAdp(ISuggestionAdp adp);
}
