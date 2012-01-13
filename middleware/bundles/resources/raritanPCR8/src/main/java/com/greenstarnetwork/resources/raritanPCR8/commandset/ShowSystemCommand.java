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
package com.greenstarnetwork.resources.raritanPCR8.commandset;

import java.util.Map;
import java.util.StringTokenizer;

import com.greenstarnetwork.models.pdu.PDUElement;
import com.greenstarnetwork.models.pdu.PDUModel;
import com.iaasframework.capabilities.commandset.AbstractCommandWithProtocol;
import com.iaasframework.capabilities.commandset.CommandException;
import com.iaasframework.capabilities.model.IResourceModel;
import com.iaasframework.capabilities.protocol.api.ProtocolErrorMessage;
import com.iaasframework.capabilities.protocol.api.ProtocolResponseMessage;
import com.iaasframework.protocols.cli.message.CLIResponseMessage;
import com.iaasframework.resources.core.message.ICapabilityMessage;

/**
 * Show system command handler. Display outlets contained by a system.
 * @author K.-K.Nguyen <synchromedia.ca>
 *
 */
public class ShowSystemCommand extends AbstractCommandWithProtocol {
//	private Logger logger = LoggerFactory.getLogger(ShowSystemCommand.class);
	public static final String COMMAND = "ShowSystemCommand";	//Query command ID
	
	public ShowSystemCommand() {
		super(COMMAND);
	}
	
	@Override
	public void executeCommand() throws CommandException{
		if (!initialized) {
			initializeWithModel();
		}else
		{
			Map<Object, Object> args = this.commandRequestMessage.getArguments();
			if (args.containsKey((String)"System")) 
			{
				sendCommandToProtocol("show " + (String)args.get((String)"System"));
			}
			else
				throw new CommandException("Invalid command argument!. Usage:ShowSystemCommand systemID");
		}
	}

	@Override
	public void initializeCommand(IResourceModel model) throws CommandException{
    	initialized = true;
	}

	@Override
	public void parseResponse(IResourceModel model) throws CommandException{
		CLIResponseMessage msg = (CLIResponseMessage)(((ProtocolResponseMessage) response).getProtocolMessage());
		if (msg == null)
			return;
		String s = msg.getRawMessage();
		
		StringTokenizer tokenizer = new StringTokenizer(s, "\n");
		String line = tokenizer.nextToken();
		
		boolean start_targets = false;
		while (line != null) 
		{
			line = line.trim();
			if (line.compareTo(PCRCommandList.PROMPT) == 0)
				break;
			if (start_targets) {
				boolean add_new = false;
				if (line.indexOf("outlet") > 0)
				{
					PDUElement elem = ((PDUModel)model).getOutlet(line);
					if (elem == null) {
						elem = new PDUElement();
						add_new = true;
					}
					elem.setID(line);
					if (add_new)
						((PDUModel)model).addOutlet(elem);
				}else if (line.indexOf("sensor") > 0)
				{
					PDUElement elem = ((PDUModel)model).getInFeed(line);
					if (elem == null) {
						elem = new PDUElement();
						add_new = true;
					}
					elem.setID(line);
					if (add_new)
						((PDUModel)model).addInfeed(elem);
				}
			}
			else if (line.startsWith("Targets:"))
				start_targets = true;
				
			line = tokenizer.nextToken();
		}
		
	}

	@Override
	public void responseReceived(ICapabilityMessage response) throws CommandException{
		if (response instanceof ProtocolResponseMessage) {
			this.response = (ProtocolResponseMessage) response;
			this.requestEngineModel(false);
		}
		else if (response instanceof ProtocolErrorMessage) {
			this.errorMessage = (ProtocolErrorMessage) response;
			CommandException commandException = new CommandException("Error executing command "
					+ this.commandID, ((ProtocolErrorMessage) response)
					.getProtocolException());
			commandException.setName(this.commandID);
			commandException.setCommand(this);
			this.sendCommandErrorResponse(commandException);
		}
	}
}