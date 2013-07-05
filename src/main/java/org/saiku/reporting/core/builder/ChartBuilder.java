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
