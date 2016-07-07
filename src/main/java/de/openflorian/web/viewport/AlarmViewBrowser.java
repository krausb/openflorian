package de.openflorian.web.viewport;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletLayer;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import de.openflorian.alarm.AlarmContextVerticle;
import de.openflorian.data.model.Operation;
import de.openflorian.util.StringUtils;
import de.openflorian.web.AbstractBrowser;
import de.openflorian.web.WebGlobals;
import de.openflorian.web.velocity.VelocityUtils;

/**
 * Operation View Controller<br/>
 * <Br/>
 * ZUL: webapp/operation/views/alarm.zul
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class AlarmViewBrowser extends AbstractBrowser implements PollListener {
	private static final long serialVersionUID = -3937904151161574461L;

	private static final Logger log = LoggerFactory.getLogger(AlarmViewBrowser.class);

	private final Audio alarmsound = new Audio();
	private final Label alarmTime = new Label();

	private final Label keyword = new Label();
	private final Label buzzword = new Label();
	private final Label currentTime = new Label();
	private final Label operationNr = new Label();
	private final Label positionLongitude = new Label();
	private final Label positionLatitude = new Label();
	private final Label object = new Label();
	private final Label street = new Label();
	private final Label crossway = new Label();
	private final Label city = new Label();
	private final Label priority = new Label();
	private final Label resourcesRaw = new Label();

	private final LMap map = new LMap();
	private final LeafletLayer mapLayer = new LOpenStreetMapLayer();

	private final CssLayout resourcesBox = new CssLayout();

	private CustomLayout layout;

	// private Gmaps operationMap;
	// private Ginfo operationMarker;

	private Button arrange;

	private final Operation currentOperation;

	public AlarmViewBrowser(UI view, Operation o) {
		super(view);
		this.currentOperation = o;
	}

	@Override
	public void init() {
		try {
			layout = new CustomLayout(
					VelocityUtils.getRenderedTemplate("VAADIN/themes/" + view.getTheme() + "/layouts/alarm.html"));

			// init layout
			layout.addComponent(alarmTime, "alarmTime");
			layout.addComponent(priority, "priority");

			layout.addComponent(keyword, "keyword");
			layout.addComponent(buzzword, "buzzword");
			layout.addComponent(currentTime, "currentTime");
			layout.addComponent(operationNr, "operationNr");
			layout.addComponent(object, "object");
			layout.addComponent(city, "city");
			layout.addComponent(resourcesRaw, "resourcesRaw");
			layout.addComponent(resourcesBox, "resources");
			layout.addComponent(map, "map");

			map.addLayer(mapLayer);

			this.addComponent(layout);
			printAlarm(currentOperation);
		}
		catch (final Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper: Print given {@link Operation}
	 */
	private void printAlarm(final Operation o) {

		// operationMap.setVisible(true);

		final Operation currentOperation = AlarmContextVerticle.getInstance().getCurrentOperation();

		if (currentOperation.isFireOperation())
			layout.setStyleName(Operation.FIRE_ZCLASS);
		else
			layout.setStyleName(Operation.TECHNICAL_ASSISTANCE_ZCLASS);

		currentTime.setValue(new SimpleDateFormat(WebGlobals.FORMAT_DATETIME).format(new Date()));

		if (currentOperation.getKeyword() != null)
			keyword.setValue(currentOperation.getKeyword());

		if (currentOperation.getBuzzword() != null)
			buzzword.setValue(currentOperation.getBuzzword());

		if (!StringUtils.isEmpty(currentOperation.getPriority()))
			priority.setValue(currentOperation.getPriority());

		if (currentOperation.getOperationNr() != null)
			operationNr.setValue(currentOperation.getOperationNr());

		if (!StringUtils.isEmpty(currentOperation.getObject())) {
			object.setValue(currentOperation.getObject());
			// objectBox.setVisible(true);
		}
		else {
			// objectBox.setVisible(false);
		}

		if (currentOperation.getPositionLatitude() != 0)
			positionLatitude.setValue(String.valueOf(currentOperation.getPositionLatitude()));

		if (currentOperation.getPositionLongitude() != 0)
			positionLongitude.setValue(String.valueOf(currentOperation.getPositionLongitude()));

		if (currentOperation.getPositionLatitude() != 0 && currentOperation.getPositionLongitude() != 0) {

			map.setCenter(currentOperation.getPositionLatitude(), currentOperation.getPositionLongitude());
			final LMarker marker = new LMarker(currentOperation.getPositionLatitude(),
					currentOperation.getPositionLongitude());
			marker.setPopup("Einsatzort");
			marker.openPopup();
			map.addComponent(marker);
			map.setWidth("100%");
			map.setHeight("100%");
			map.setZoomLevel(16);
		}
		else {
			map.setVisible(false);
		}

		if (!StringUtils.isEmpty(currentOperation.getStreet())) {
			street.setValue(currentOperation.getStreet());
		}

		if (!StringUtils.isEmpty(currentOperation.getCity())) {
			city.setValue(currentOperation.getCity());
		}

		if (currentOperation.getResourcesRaw() != null)
			resourcesRaw.setValue(currentOperation.getResourcesRaw());

		// TODO: do resources
		// for (OperationResource resource : currentOperation.getResources()) {
		// if (resource == null)
		// continue;
		//
		// if (log.isDebugEnabled())
		// log.debug("Alarmed resource: " + resource);
		//
		// Label resourceLabel = new Label(resource.getCallName());
		// resourceLabel.setStyleName("operation-resource");
		// resourcesBox.addComponent(resourceLabel);
		// }

		final SimpleDateFormat format = new SimpleDateFormat(WebGlobals.FORMAT_DATETIME);
		if (currentOperation.getIncurredAt() != null)
			alarmTime.setValue(format.format(currentOperation.getIncurredAt()));

		alarmsound.play();
	}

	@Override
	public void close() {

	}

	@Override
	public void poll(PollEvent event) {
		// TODO Auto-generated method stub

	}

}
