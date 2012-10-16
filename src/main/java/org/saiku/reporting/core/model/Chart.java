package org.saiku.reporting.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name="chart")
@XmlAccessorType(XmlAccessType.FIELD)
public class Chart {

	@XmlElement(name="cgg-url")
	@JsonProperty("cggUrl")
	private String cgg;

	public void setCgg(String cgg) {
		this.cgg = cgg;
	}

	public String getCgg() {
		return cgg;
	}
	
}
