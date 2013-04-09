/**
 * 
 */
package org.saiku.reporting.core.adapter;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author mg
 *
 */
public class ColorAdapter extends XmlAdapter<String,Color> {
	
	public Color unmarshal(String s) {
		return Color.decode(s.replaceFirst("#", "0x"));
	}
	public String marshal(Color c) {
		int colInt = c.getRGB();
		String str = Integer.toHexString(colInt);
		return str.replaceFirst("ff", "#");
	}
}
