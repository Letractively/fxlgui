/**
 * Copyright (c) 2010 Dangelmayr IT GmbH. All rights reserved.
 *  
 * This file is part of FXL GUI API.
 *  
 * FXL GUI API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * FXL GUI API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with FXL GUI API.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.fxl.gui.wizard.impl;

import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.wizard.api.IWizardStepCard;

class WizardStepCard implements IWizardStepCard {

	private WizardStepImpl wizardStepImpl;
	IVerticalPanel contentPanel;
	IVerticalPanel sideWidgetPanel;
	private IClickable<?> clickable;

	void setWizardStep(WizardStepImpl wizardStepImpl) {
		this.wizardStepImpl = wizardStepImpl;
		contentPanel = this.wizardStepImpl.contentPanelCards.add().panel()
				.vertical();
		sideWidgetPanel = this.wizardStepImpl.mainSideWidgetCards.add().panel()
				.vertical();
	}

	@Override
	public IVerticalPanel panel() {
		return contentPanel;
	}

	@Override
	public IVerticalPanel sideWidgetPanel() {
		return sideWidgetPanel;
	}

	@Override
	public IWizardStepCard title(String title) {
		if (clickable != null) {
			// TODO ... clickable.text(title);
			throw new MethodNotImplementedException();
		}
		clickable = this.wizardStepImpl.widgetTitle.addHyperlink(title);
		clickable.addClickListener(new IClickListener() {

			@Override
			public void onClick() {
				activate();
			}
		});
		return this;
	}

	void activate() {
		wizardStepImpl.contentPanel = contentPanel;
		wizardStepImpl.sideWidgetPanel = sideWidgetPanel;
		wizardStepImpl.contentPanelCards.show(contentPanel);
		wizardStepImpl.mainSideWidgetCards.show(sideWidgetPanel);
		for (WizardStepCard card : wizardStepImpl.cards)
			card.clickable.clickable(true);
		if (clickable != null)
			clickable.clickable(false);
	}

	@Override
	public IWizardStepCard clear() {
		contentPanel.clear();
		sideWidgetPanel.clear();
		return this;
	}
}