package co.fxl.gui.log.impl;

import co.fxl.gui.i18n.api.DontTranslate;

public aspect LogImplI18N {

	declare @method : LogImpl.onClick() : @DontTranslate;
}
