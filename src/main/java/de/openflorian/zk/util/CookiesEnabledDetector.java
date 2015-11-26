package de.openflorian.zk.util;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuService;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;

/**
 * Async Cookies Enabled Detector
 * 
 * Triggers Client Javascript for checking if cookies are enabled
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public class CookiesEnabledDetector implements AuService {

	private static Logger log = LoggerFactory.getLogger(CookiesEnabledDetector.class);
	
	private static ThreadLocal<Boolean> cookiesEnabled = new ThreadLocal<Boolean>();
	
	@SuppressWarnings("rawtypes")
	private static List<EventListener> eventListener = new LinkedList<EventListener>();
	
	/**
	 * AuService for collecting checkCookie() JS function result
	 */
	public boolean service(AuRequest request, boolean everError) {
		String command = request.getCommand();
		if (OnCookieStateEvent.COOKIE_STATE_EVENT.equals(command)) {
			cookiesEnabled.set(Boolean.valueOf(request.getData().get("result").toString()));
			log.trace("Cookies enabled (raw): " + request.getData().get("result").toString());
			notifyListener(cookiesEnabled.get());
			return true;
		}
		return false;
	}
	
	/**
	 * Helper: Send JavaScript Call to Server
	 * 
	 * @param script
	 * @return
	 */
	public void asyncTriggerCheckForCookies() {
		String script = "__data=checkCookie();"
				+ "zAu.send(new zk.Event(null,\"" + OnCookieStateEvent.COOKIE_STATE_EVENT + "\",{result:__data},{toServer:true}));"; 
		Clients.evalJavaScript(script);
	}
	
	/**
	 * Add event listener
	 * @param listener
	 */
	@SuppressWarnings("rawtypes")
	public static void addEventListener(EventListener listener) {
		eventListener.add(listener);
	}
	
	/**
	 * Notify available eventlisteners about cookies state
	 * @param cookiesEnabled
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void notifyListener(boolean cookiesEnabled) {
		for(EventListener listener: eventListener) {
			try {
				listener.onEvent(new OnCookieStateEvent(cookiesEnabled));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Debug: Print all cookies to log
	 */
	public static void printCookies() {
		Cookie [] cookies = ((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie c: cookies)
				log.trace("Cookie: name=" + c.getName() + "; domain=" + c.getDomain() + "; path=" + c.getPath() + "; age=" + c.getMaxAge());				
		}
	}
	
}
