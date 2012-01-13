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
package com.greenstarnetwork.tests.services.controller;

/**
 * This class will test the MigrationPlan class. 
 * 
 * 
 * @author Fereydoun Farrahi Moghaddam (ffarrahi@synchromedia.ca)
 *
 */

import junit.framework.TestCase;

import com.greenstarnetwork.services.controller.model.Migration;
import com.greenstarnetwork.services.controller.model.MigrationPlan;
import com.iaasframework.core.internal.persistence.utilities.Assert;




public class MigrationPlanTest extends TestCase {

	/**
	 * 
	 */
	protected void setUp() throws Exception {
		
	}

	/**
	 * 
	 */
	public void testMigrationPlan() {
		MigrationPlan migrationPlan = new MigrationPlan();
		Migration migration = new Migration();
		migrationPlan.addMigration(migration);
		migrationPlan.setupIterator();
		if (migrationPlan.hasNextMigration()){
			migration = migrationPlan.getNextMigration();
		}
		Assert.notNull(migration, "migration obj is null");
	}
}