/*******************************************************************************
 * Copyright 2013 Marius Giepz and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.saiku.reporting.core.builder;

import java.util.ArrayList;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.Group;
import org.pentaho.reporting.engine.classic.core.GroupBody;
import org.pentaho.reporting.engine.classic.core.RelationalGroup;
import org.pentaho.reporting.engine.classic.core.SubGroupBody;

public class GroupUtils {
	
	/**
	 * Removes the unusedTemplateGroups based on the assumption that if a group
	 * doesn't have any fields assigned to it that it is empty.
	 * @param definition 
	 * 
	 * @param definition
	 */
	public static void removedUnusedTemplateGroups(final int groupsDefined, AbstractReportDefinition definition) {
		final RelationalGroup[] templateRelationalGroups = getTemplateRelationalGroups(definition);
		final int templateRelationalGroupCount = templateRelationalGroups.length;
		for (int i = groupsDefined; i < templateRelationalGroupCount; i++) {
			final RelationalGroup templateRelationalGroup = templateRelationalGroups[i];
			definition.removeGroup(templateRelationalGroup);
		}
	}

	/**
	 * @param definition
	 * @return the relational groups in the templates in a flattened array.
	 */
	private static RelationalGroup[] getTemplateRelationalGroups(AbstractReportDefinition definition) {
		final ArrayList<RelationalGroup> relationalGroups = new ArrayList<RelationalGroup>();
		Group group = definition.getRootGroup();
		while (group != null && group instanceof RelationalGroup) {
			relationalGroups.add((RelationalGroup) group);
			final GroupBody body = group.getBody();
			if (body instanceof SubGroupBody) {
				final SubGroupBody sgBody = (SubGroupBody) body;
				if (sgBody.getGroup() instanceof RelationalGroup) {
					group = sgBody.getGroup();
				} else {
					group = null;
				}
			} else {
				group = null;
			}
		}

		return relationalGroups.toArray(new RelationalGroup[relationalGroups.size()]);
	}

}
