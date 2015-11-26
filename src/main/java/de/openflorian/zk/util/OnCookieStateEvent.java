package de.openflorian.zk.util;

import org.zkoss.zk.ui.event.Event;

/**
 * OnCookieStateEvent will be fired to listeners of an {@link CookiesEnabledDetector} instance<br/>
 * after triggering {@link CookiesEnabledDetector#asyncTriggerCheckForCookies()}
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class OnCookieStateEvent extends Event {
	private static final long serialVersionUID = 3199730361122291122L;

	protected static final String COOKIE_STATE_EVENT = "onCookieState";
	
	private final boolean state;
	
	public OnCookieStateEvent(boolean state) {
		super("OnCookieStateEvent");
		this.state = state;
	}
	
	public boolean getState() {
		return state;
	}
	
}
