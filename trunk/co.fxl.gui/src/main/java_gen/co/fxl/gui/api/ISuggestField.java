package co.fxl.gui.api;

import co.fxl.gui.api.ISuggestField.ISource;

import java.util.List;


public interface ISuggestField extends IKeyRecipient<co.fxl.gui.api.ISuggestField>,
    ITextInput<co.fxl.gui.api.ISuggestField> {
    ISuggestField source(ISource source);

    ISuggestField addSuggestionListener(
        IUpdateable.IUpdateListener<co.fxl.gui.api.ISuggestField.ISource.ISuggestion> selection);

    public interface ISource {
        void query(String prefix,
            ICallback<java.util.List<co.fxl.gui.api.ISuggestField.ISource.ISuggestion>> callback);

        public interface ISuggestion {
            String insertText();

            String displayText();
        }
    }
}
