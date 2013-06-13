package org.saiku.reporting.core.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="map")
@XmlAccessorType(XmlAccessType.FIELD)
class MapElements {
	@XmlAttribute
	public String property;
	@XmlAttribute
	public String value;

	@SuppressWarnings("unused")
	private MapElements() {
	} // Required by JAXB

	public MapElements(String key, String value) {
		this.property = key;
		this.value = value;
	}
}
