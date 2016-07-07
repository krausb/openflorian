package de.openflorian.alarm.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.openflorian.config.OpenflorianConfig;
import de.openflorian.data.model.Operation;
import de.openflorian.util.StringUtils;

/**
 * Street Parser Responsable
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
class ResourcesParserResponsable extends AlarmFaxParserPatternMatcherResponsable {

	protected Pattern callNamePattern = null;
	protected String stationresourcePattern = null;

	public ResourcesParserResponsable() {
		super();

		try {
			final String callNamePatternStr = OpenflorianConfig.config().faxParser.patterns.resourcecallnamePattern;
			if (!StringUtils.isEmpty(callNamePatternStr)) {
				callNamePattern = Pattern.compile(callNamePatternStr);
				log.info("Resource callName pattern: " + callNamePatternStr);
			}
			else {
				throw new IllegalStateException("Resource callName pattern not set in configuration file.");
			}

			stationresourcePattern = OpenflorianConfig.config().faxParser.patterns.stationresourcePattern;
			if (StringUtils.isEmpty(stationresourcePattern))
				throw new IllegalStateException("Station resource pattern not set in configuration file.");
			else
				log.info("Station resource pattern: " + stationresourcePattern);
		}
		catch (final Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile(OpenflorianConfig.config().faxParser.patterns.resourcesPattern,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CASE);
	}

	@Override
	public void parse(String alarmfax, Operation operation) {
		final Matcher m = getPattern().matcher(alarmfax);
		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			sb.append(m.group(1));
			sb.append(System.getProperty("line.separator"));

			if (m.group(1).contains(stationresourcePattern)) {
				final Matcher callNameMatcher = callNamePattern.matcher(m.group(1));
				// if (callNameMatcher.find()) {
				// OperationResource resource = OperationResourceService.transactional()
				// .getResourceByCallname(callNameMatcher.group(1));
				// if (operation.getResources() == null)
				// operation.setResources(new ArrayList<OperationResource>());
				// operation.getResources().add(resource);
				// }
			}
		}
		operation.setResourcesRaw(sb.toString());

		if (getNext() != null)
			getNext().parse(alarmfax, operation);
	}

}
