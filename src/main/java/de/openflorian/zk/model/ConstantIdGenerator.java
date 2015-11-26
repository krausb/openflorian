package de.openflorian.zk.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.sys.IdGenerator;

/**
 * Constant ID Generator
 * <br/>
 * Generiert konstante, vorhersagbare IDs
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class ConstantIdGenerator implements IdGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String nextDesktopId( Desktop desktop ) {
        if (desktop.getAttribute("Id_Num") == null) {
            String number = "0";
            desktop.setAttribute("Id_Num", number);
        }
        return null;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String nextPageUuid( Page page ) {
        return null;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String nextComponentUuid(Desktop desktop, Component comp,
			ComponentInfo compInfo) {
        int i = Integer.parseInt(desktop.getAttribute("Id_Num").toString());
        i++;// Start from 1
        desktop.setAttribute("Id_Num", String.valueOf(i));
        return "zk_comp_" + i;
	}

}
