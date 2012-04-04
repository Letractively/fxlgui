package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.api.ILabel;
import co.fxl.gui.i18n.impl.DontTranslate;
import co.fxl.gui.i18n.impl.I18NAspect;

public aspect ColumnSelectionI18N extends I18NAspect {

	declare @method : ILabel ColumnSelection.decoratePanel(..) : @DontTranslate;
}
