package de.openflorian.web.viewport;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.leaflet.*;

import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.ui.*;

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

	private Audio alarmsound = new Audio();
	private Label alarmTime = new Label();

	private Label keyword = new Label();
	private Label buzzword = new Label();
	private Label currentTime = new Label();
	private Label operationNr = new Label();
	private Label positionLongitude = new Label();
	private Label positionLatitude = new Label();
	private Label object = new Label();
	private Label street = new Label();
	private Label crossway = new Label();
	private Label city = new Label();
	private Label priority = new Label();
	private Label resourcesRaw = new Label();

	private LMap map = new LMap();
	private LeafletLayer mapLayer = new LOpenStreetMapLayer();

	private VerticalLayout resourcesBox;

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
			layout.addComponent(map, "map");

			map.addLayer(mapLayer);

			this.addComponent(layout);
			printAlarm(currentOperation);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper: Print given {@link Operation}
	 */
	private void printAlarm(final Operation o) {

		// operationMap.setVisible(true);

		Operation currentOperation = AlarmContextVerticle.getInstance().getCurrentOperation();

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
		} else {
			// objectBox.setVisible(false);
		}

		if (currentOperation.getPositionLatitude() != 0)
			positionLatitude.setValue(String.valueOf(currentOperation.getPositionLatitude()));

		if (currentOperation.getPositionLongitude() != 0)
			positionLongitude.setValue(String.valueOf(currentOperation.getPositionLongitude()));

		if (currentOperation.getPositionLatitude() != 0 && currentOperation.getPositionLongitude() != 0) {

			map.setCenter(currentOperation.getPositionLatitude(), currentOperation.getPositionLongitude());
			LMarker marker = new LMarker(currentOperation.getPositionLatitude(),
					currentOperation.getPositionLongitude());
			marker.setPopup("Einsatzort");
			marker.openPopup();
			map.addComponent(marker);
			map.setWidth("100%");
			map.setHeight("100%");
			map.setZoomLevel(16);
		} else {
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

		SimpleDateFormat format = new SimpleDateFormat(WebGlobals.FORMAT_DATETIME);
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
