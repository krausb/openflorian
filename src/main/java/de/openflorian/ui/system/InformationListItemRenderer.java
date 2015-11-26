package de.openflorian.ui.system;

import java.util.Map.Entry;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 * {@link Entry<String, String>} Listbox Listitem Renderer
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class InformationListItemRenderer implements ListitemRenderer<Entry<String, String>> {

	public void render(Listitem item, Entry<String, String> entry, int index) throws Exception {
		item.setValue(entry);
		
		Listcell key = new Listcell(entry.getKey());
		key.setParent(item);
		
		Listcell value = new Listcell(entry.getValue());
		value.setParent(item);		
	}

}
