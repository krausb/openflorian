package de.openflorian.web;

import java.security.Principal;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract Browser
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public abstract class AbstractBrowser extends VerticalLayout {
	private static final long serialVersionUID = 2536853917292334504L;

	protected final UI view;

	public AbstractBrowser(UI view) {
		this.view = view;
	}

	public boolean mayView(Principal user) {
		return true;
	}

	/**
	 * Init browser
	 */
	public abstract void init();

	/**
	 * Detach browser
	 */
	public abstract void close();

}
