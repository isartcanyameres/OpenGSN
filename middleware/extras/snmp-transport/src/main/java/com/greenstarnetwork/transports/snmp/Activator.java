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
package com.greenstarnetwork.transports.snmp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This bundle's activator. Mainly used to get the bundle context
 * @author edu
 *
 */
public class Activator implements BundleActivator{
	
	private static BundleContext context;

	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public static BundleContext getContext(){
		return context;
	}

}