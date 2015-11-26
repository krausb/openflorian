package de.openflorian.alarm.parser;

import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import de.openflorian.config.ConfigurationProvider;
import de.openflorian.data.model.Operation;

/**
 * A pattern matcher responsable for realizing a chain of 
 * responsibility to match the elements of the alarm fax text.<br/>
 * <br/>
 * Each {@link AlarmFaxParserPatternMatcherResponsable} matches a 
 * single Alarm Fax attribute, puts the result into the passed
 * {@link Operation} and plays the ball to the 
 * in chain.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public abstract class AlarmFaxParserPatternMatcherResponsable implements InitializingBean {

	protected AlarmFaxParserPatternMatcherResponsable next = null;
	
	protected Pattern parserPattern = null;
	
	@Autowired(required=true)
	protected ConfigurationProvider config = null;
	
	/**
	 * Gets the next {@link AlarmFaxParserPatternMatcherResponsable} in chain
	 * or null if the end of the chain is reached.
	 * 
	 * @return
	 * 		{@link AlarmFaxParserPatternMatcherResponsable}
	 */
	public AlarmFaxParserPatternMatcherResponsable getNext() {
		return next;
	}
	
	public abstract String getConfigurationProperty();
	
	@Override
	public void afterPropertiesSet() {
		this.parserPattern = Pattern.compile(config.getProperty(getConfigurationProperty()), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CASE);
	}
	
	/**
	 * Sets the next {@link AlarmFaxParserPatternMatcherResponsable} in chain
	 * @param next
	 */
	public void setNext(AlarmFaxParserPatternMatcherResponsable next) {
		this.next = next;
	}
	
	/**
	 * Getter for the parsing pattern.
	 * 
	 * @return
	 */
	public abstract Pattern getPattern();
	
	/**
	 * Processes the given <code>alarmfax</code> and stores the responsable 
	 * attribute in the target <code>operation</code> entity.
	 * 
	 * @param alarmfax
	 * 		{@link String}
	 * @param operation
	 * 		{@link Operation}
	 */
	public abstract void parse(String alarmfax, Operation operation);
	
}
