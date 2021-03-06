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

import java.net.MalformedURLException;
import java.net.URL;

import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.elementfactory.ContentFieldElementFactory;

public class ChartBuilder {
	
	public static Element build(){
		ContentFieldElementFactory factory = new ContentFieldElementFactory();
		try {
			factory.setBaseURL(new URL("http://lakdj"));
			factory.setFieldname("abc");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
//		System.out.println(
//		    	(String) element.getAttribute("http://reporting.pentaho.org/namespaces/engine/attributes/core", "content-base")
//		    	);
//		    	
//		    	element.setAttribute(AttributeNames.Core.NAMESPACE, AttributeNames.Core.VALUE, 
//		    		"http://code-inside.de/blog/wp-content/uploads/image570.png");
		
		return factory.createElement();
	}

}
