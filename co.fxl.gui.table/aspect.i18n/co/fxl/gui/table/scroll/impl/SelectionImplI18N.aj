package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.i18n.impl.I18NAspect;
import co.fxl.gui.i18n.impl.Translate;

public aspect SelectionImplI18N extends I18NAspect {

	declare @method : void SelectionImpl.update() : @Translate;

}
