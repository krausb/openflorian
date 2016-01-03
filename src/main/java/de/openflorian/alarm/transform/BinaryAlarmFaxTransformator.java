package de.openflorian.alarm.transform;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import de.openflorian.alarm.AlarmFaxIncomingEvent;
import de.openflorian.event.EventQueue;

/**
 * Binary Alarm Fax Transformator<br/>
 * <br/>
 * Processes the given alarm fax with an OCR binary like
 * tesseract.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class BinaryAlarmFaxTransformator extends AbstractAlarmFaxTransformator {
	
	/* (non-Javadoc)
	 * @see de.openflorian.alarm.transform.AbstractAlarmFaxTransformator#transform(de.openflorian.alarm.AlarmFaxIncomingEvent)
	 */
	@Override
	public void transform(AlarmFaxIncomingEvent event) {
		Runtime rt = Runtime.getRuntime();
		
		try {
			String resultTextFile = String.format("%s/%s",
					faxObervationDirectory,
					event.getResultFile().getName());
			
			String cmd = "";
			cmd = transformationCmd.replaceAll(
					transformationCmdVarInput, 
					event.getResultFile().getAbsolutePath().replace("\\", "/"));
			cmd = cmd.replaceAll(
					transformationCmdVarOutput, 
					resultTextFile);

			log.debug("Running cmd: " + cmd);
			
			Process ps = rt.exec(cmd);
			
			int exitCode = ps.waitFor();
			if(exitCode == 0) {
				log.debug(String.format("Successful transformed '%s' to '%s'. Exit code: %d",
						event.getResultFile().getAbsolutePath(),
						resultTextFile,
						exitCode));
				EventQueue.getInstance().publish(new AlarmFaxTransformedEvent(new File(resultTextFile + ".txt")));
			} else {
				log.error(String.format("An error occured transforming '%s'. Exit code: %d",
						event.getResultFile().getAbsolutePath(),
						exitCode));
				log.error(IOUtils.toString(ps.getErrorStream(), "UTF-8"));
			}
			ps.destroy();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}
	
}
