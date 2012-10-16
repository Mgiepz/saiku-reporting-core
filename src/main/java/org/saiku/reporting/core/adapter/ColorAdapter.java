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
		return Color.decode(s);
	}
	public String marshal(Color c) {
		return "#"+Integer.toHexString(c.getRGB());
	}
}
