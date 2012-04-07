package co.fxl.gui.api;

public interface IContainer {
    ILayout panel();

    IButton button();

    IImage image();

    ILabel label();

    ITextField textField();

    ISuggestField suggestField();

    IPasswordField passwordField();

    ITextArea textArea();

    IRichTextArea richTextArea();

    ICheckBox checkBox();

    IComboBox comboBox();

    IRadioButton radioButton();

    IHorizontalLine line();

    IToggleButton toggleButton();

    IScrollPane scrollPane();

    ISplitPane splitPane();

    IHyperlink hyperlink();

    IElement<?> element(IElement<?> element);

    IElement<?> nativeElement(Object object);

    IElement<?> element();

    <T> T widget(Class<T> interfaceClass);

    IContainer clear();

    IDisplay display();
}
