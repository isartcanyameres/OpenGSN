/**
 * Copyright 2009-2011 École de technologie supérieure,
 * Communication Research Centre Canada,
 * Inocybe Technologies Inc. and 6837247 CANADA Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greenstarnetwork.resources.raritanclimate.commandset;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.climate.Climate;
import com.greenstarnetwork.models.climate.TemperatureElement;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Get Temperature Command implementation
 * 
 * 
 * @author K.-K.Nguyen <synchromedia.ca>
 * @author A.Daouadji <synchromedia.ca>
 */

public class GetTemperatureCommand extends AbstractCommandWithProtocol {
	public static final String COMMAND = "GetTemperatureCommand";
String nm;
	// Query command ID
Climate climate;
	public GetTemperatureCommand() {
		super(COMMAND);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeCommand() throws CommandException {

		if (!initialized) {
			initializeWithModel();
		} else {
			String command = PCRCommandList.SHOW;
			Map<Object, Object> args = this.commandRequestMessage
					.getArguments();
			if (args.containsKey((String) "Sensor"))
				command += " " + (String) args.get((String) "Sensor");

			else
				throw new CommandException(
						"Invalid command argument!. Usage:GetTemperatureCommand sensor_ID");

			sendCommandToProtocol(command);
		}

	}

	@Override
	public void initializeCommand(IResourceModel arg0) throws CommandException {
		initialized = true;

	}

	@Override
	public void parseResponse(IResourceModel arg0) throws CommandException {

		//System.err.println("********* Starting parsing  GetTemperature command response \n");
		CLIResponseMessage msg = (CLIResponseMessage) ((ProtocolResponseMessage) response)
				.getProtocolMessage();
		String s = msg.getRawMessage();
		climate =((Climate) model);
		try {
			StringTokenizer tokenizer = new StringTokenizer(s, "\n");
			String line = tokenizer.nextToken().trim(); // first line is the
														// command
			while (!line.startsWith("/"))
				line = tokenizer.nextToken().trim();
			TemperatureElement elem = ((Climate) model)
					.getTemperatureelement(line);
			if (elem == null)
				throw new CommandException("Sensor not found: " + line);
			line = tokenizer.nextToken().trim();

			while (line != null) {
				if (line.startsWith("Name is")){
					String ls=null;
					if (line.length()>15)
					nm =line.substring(new String("Name is").length() + 1);
					
					elem.setName(nm);
				}
				else if (line.startsWith("CurrentReading is")) {
					String ls=null;
					if (line.length()>17)				
					 ls = line.substring(new String("CurrentReading is")
							.length() + 1);
					
					if ("-20.000000".equals(ls) ){
						
						 ((Climate) model).removeTemp(elem.getID());
					}else
					
					{
					
						elem.setTemperature(ls);
//					String tl = climate.getTemperatureLowerThreshold();
//					 String tu = climate.getTemperatureUpperThreshold();
//					
//					if(ls!=null)
//					{
//						if (Float.valueOf(ls.trim()).floatValue()<= Float.valueOf(tl.trim()).floatValue()){
//                    	Alarm alarm = new Alarm();
//                    	alarm.setDescription("the current Temperature ("+ ls+"C) is less than threshold ("+tl+"C)");
//                    	alarm.setAlarm("LOW TEMPERATURE");
//                    	alarm.setTime(Calendar.getInstance());
//                    	alarm.setId(elem.getID());
//                    	((Climate) model).addclimate(alarm);
//                    }
//                    if (Float.valueOf(ls.trim()).floatValue()>= Float.valueOf(tu.trim()).floatValue()){
//                    	Alarm alarm = new Alarm();
//                    	alarm.setDescription("the current Temperature ("+ ls+" C) is high than threshold ("+tu+" C)");
//                    	alarm.setAlarm("TEMPERATURE HIGH");
//                    	alarm.setId(elem.getID());
//                    	alarm.setTime(Calendar.getInstance());
//                    	((Climate) model).addclimate(alarm);
//                    }
//					}
					
					}
		
				
				} else if (line.startsWith("CurrentState is")) {
					String ls=null;
					if (line.length()>15)
					 ls = line.substring(new String("CurrentState is")
							.length() + 1);
				
					elem.setCurrentState(ls);

				}else if (line.startsWith("SensorType is")) 
				{String ls=null ;
				if (line.length()>13)
			   ls = line.substring(new String("SensorType is").length() + 1);
				if (!("2 (Temperature)".equals(ls))){
				 ((Climate) model).removeTemp(elem.getID());
				}
		
			}

				line = tokenizer.nextToken().trim();
			}
		} catch (NoSuchElementException e) {
		}

	}

	@Override
	public void responseReceived(ICapabilityMessage response)
			throws CommandException {
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		} else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException(
					"Error executing command " + this.commandID,
					((ProtocolErrorMessage) response).getProtocolException());
			commandException.setName(this.commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}

}