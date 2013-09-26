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
