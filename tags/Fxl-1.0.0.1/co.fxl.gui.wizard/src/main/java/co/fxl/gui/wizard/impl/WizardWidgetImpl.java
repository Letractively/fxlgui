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

import java.util.LinkedList;
import java.util.List;

import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.impl.WidgetTitle;
import co.fxl.gui.wizard.api.IWizardStep;
import co.fxl.gui.wizard.api.IWizardWidget;

class WizardWidgetImpl implements IWizardWidget {

	class NextClickListener implements IClickListener {
		@Override
		public void onClick() {
			next();
		}
	}

	class PreviousClickListener implements IClickListener {
		@Override
		public void onClick() {
			previous();
		}
	}

	static final int SPACING = 10;
	static final int SPAN = 5;
	static final int BUTTON_SIZE = 50;
	ICardPanel cards;
	private IClickable<?> nextButton;
	private IClickable<?> previousButton;
	List<WizardStepImpl> steps = new LinkedList<WizardStepImpl>();
	int stepIndex = 0;
	private WidgetTitle widgetTitle;
	IVerticalPanel stepPanel;
	String title;
	ICardPanel sideWidgetCards;

	WizardWidgetImpl(ILayout layout) {
		IVerticalPanel panel = layout.vertical().spacing(SPACING);
		widgetTitle = new WidgetTitle(panel.add().panel());
		sideWidgetCards = panel.add().panel().card();
		addPrevious();
		addNext();
		stepPanel = widgetTitle.content().panel().vertical().spacing(4);
	}

	@Override
	public IWizardWidget content(ILayout layout) {
		cards = layout.card();
		return this;
	}

	private void addPrevious() {
		previousButton = widgetTitle.addHyperlink("<<");
		previousButton.addClickListener(new PreviousClickListener());
		previousButton.clickable(false);
	}

	private void addNext() {
		nextButton = widgetTitle.addHyperlink(">>");
		nextButton.addClickListener(new NextClickListener());
		nextButton.clickable(false);
	}

	void next(int offset) {
		jumpTo(stepIndex, stepIndex + offset);
	}

	void jumpTo(int previous, int next) {
		stepIndex = next;
		steps.get(previous).highlight(false);
		steps.get(previous).panel().visible(false);
		steps.get(next).panel().visible(true);
		steps.get(next).highlight(true);
		updateButtons();
	}

	void jumpToStep(WizardStepImpl wizardStepImpl) {
		int index = steps.indexOf(wizardStepImpl);
		jumpTo(stepIndex, index);
	}

	void updateButtons() {
		clickable(previousButton, stepIndex != 0);
		clickable(nextButton, steps.get(stepIndex).checked);
		for (WizardStepImpl step : steps) {
			step.clickable(steps.get(stepIndex).checked
					&& stepIndex + 1 == step.stepIndex);
		}
	}

	private void clickable(IClickable<?> button, boolean clickable) {
		button.clickable(clickable);
	}

	@Override
	public IWizardWidget title(String title) {
		widgetTitle.addTitle(title);
		this.title = title;
		return this;
	}

	@Override
	public IWizardStep addStep() {
		WizardStepImpl step = new WizardStepImpl(this, steps.size());
		steps.add(step);
		return step;
	}

	@Override
	public IWizardWidget visible(boolean visible) {
		return this;
	}

	@Override
	public IWizardWidget next() {
		next(1);
		return this;
	}

	@Override
	public IWizardWidget previous() {
		next(-1);
		return this;
	}
}
