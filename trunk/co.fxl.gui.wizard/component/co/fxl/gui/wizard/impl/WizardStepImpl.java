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
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.ILayout;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.template.WidgetTitle;
import co.fxl.gui.wizard.api.IWizardStep;
import co.fxl.gui.wizard.api.IWizardStepCard;

class WizardStepImpl extends WizardStepCard implements IWizardStep,
		IClickListener {

	private WizardWidgetImpl widget;
	private ILabel textLabel;
	boolean checked = false;
	private List<IWizardStepListener> listeners = new LinkedList<IWizardStepListener>();
	int stepIndex;
	WidgetTitle widgetTitle;
	private IVerticalPanel mainPanel;
	ICardPanel mainSideWidgetCards;
	ICardPanel contentPanelCards;
	List<WizardStepCard> cards = new LinkedList<WizardStepCard>();
	private IVerticalPanel cPanel;

	WizardStepImpl(WizardWidgetImpl widget, int stepIndex) {
		this.widget = widget;
		this.stepIndex = stepIndex;
		cPanel = widget.cards.add().panel().vertical().spacing(10);
		mainPanel = cPanel.add().panel().vertical();
		mainPanel.color().white();
		mainPanel.border().color().lightgray();
		mainSideWidgetCards = widget.sideWidgetCards.add().panel().card();
		ILayout layout = mainPanel.spacing(16).add().panel();
		widgetTitle = new WidgetTitle(layout);
		widgetTitle.addTitle(widget.title + ".").font().pixel(18).weight()
				.plain().color().gray();
		contentPanelCards = widgetTitle.content().panel().card();
		IHorizontalPanel panel = widget.stepPanel.add().panel().horizontal()
				.add().panel().horizontal();
		panel.add().label().text(String.valueOf(widget.steps.size() + 1) + ".")
				.font().pixel(13).color().gray();
		panel.addSpace(4);
		textLabel = panel.add().label();
		textLabel.font().pixel(13).weight().bold();
		highlight(stepIndex == 0);
		textLabel.addClickListener(this);
		clickable(false);
		init();
	}

	private void init() {
		setWizardStep(this);
		activate();
	}

	@Override
	public IWizardStep title(String title) {
		widgetTitle.addTitle(title).font().pixel(18);
		textLabel.text(title);
		return this;
	}

	void highlight(boolean highlight) {
		if (!highlight)
			setHighlightFalse();
		else
			setHighlightTrue();
		for (IWizardStepListener listener : listeners) {
			listener.onActive(highlight);
		}
	}

	private void setHighlightFalse() {
		textLabel.font().color().gray();
	}

	private void setHighlightTrue() {
		textLabel.font().color().black();
		widget.cards.show(cPanel);
		widget.sideWidgetCards.show(mainSideWidgetCards);
	}

	@Override
	public IWizardStep checked(boolean checked) {
		this.checked = checked;
		widget.updateButtons();
		return this;
	}

	@Override
	public IVerticalPanel panel() {
		return contentPanel;
	}

	@Override
	public IWizardStep addListener(IWizardStepListener listener) {
		listeners.add(listener);
		return this;
	}

	@Override
	public IWizardStep next() {
		widget.next();
		return this;
	}

	void clickable(boolean clickable) {
		textLabel.clickable(clickable);
		if (clickable) {
			textLabel.font().underline(true).color().blue();
		} else {
			textLabel.font().underline(false);
			if (stepIndex == widget.stepIndex)
				textLabel.font().color().black();
			else
				textLabel.font().color().gray();
		}
	}

	@Override
	public void onClick() {
		widget.jumpToStep(this);
	}

	@Override
	public IVerticalPanel sideWidgetPanel() {
		return sideWidgetPanel;
	}

	@Override
	public WizardStepCard addCard() {
		WizardStepCard card = new WizardStepCard();
		card.setWizardStep(this);
		if (cards.isEmpty()) {
			card.title(String.valueOf(cards.size() + 1));
			card.activate();
		}
		cards.add(card);
		return card;
	}

	@Override
	public IWizardStepCard clear() {
		super.clear();
		contentPanelCards.clear();
		mainSideWidgetCards.clear();
		widgetTitle.clearHyperlinks();
		cards.clear();
		init();
		return this;
	}
}
