/**
 * 
 */
package org.saiku.reporting.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author mg
 *
 */
@XmlRootElement(name="rootband-format")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootBandFormat extends ElementFormat {
	
	@XmlElement(name="repeat")
	private Boolean repeat = true;
	
	@XmlElement(name="visible")
    private Boolean visible = true;
	  
	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}
		
	public Boolean isRepeat() {
		return repeat;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

}
