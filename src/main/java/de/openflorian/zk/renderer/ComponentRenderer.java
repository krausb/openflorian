package de.openflorian.zk.renderer;

import org.zkoss.zk.ui.Component;

/**
 * Interface for rendering complex ZK UI {@link Component}s.
 * 
 * @author Bastian Kraus <bk@pogo-systems.de>
 */
public interface ComponentRenderer {

	/**
	 * Render a complex {@link Component}
	 * 
	 * @return
	 * 		{@link Component}
	 */
	Component render();
	
}
