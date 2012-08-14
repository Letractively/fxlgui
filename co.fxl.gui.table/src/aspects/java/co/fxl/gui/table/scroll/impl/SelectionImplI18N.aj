package co.fxl.gui.table.scroll.impl;

import co.fxl.gui.i18n.api.Translate;
import co.fxl.gui.i18n.impl.I18NAspect;

public aspect SelectionImplI18N extends I18NAspect {

	declare @method : void SelectionImpl.update() : @Translate;

}
