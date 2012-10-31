package org.saiku.reporting.core.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name="data-field")
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldDefinition {
	
	public Map<String, HashMap<String, ElementFormat>> getElementFormats() {
		return elementFormats;
	}

	public void setElementFormats(
			HashMap<String, HashMap<String, ElementFormat>> elementFormats) {
		this.elementFormats = elementFormats;
	}

	public FieldDefinition() {
		this.headerFormat = new ElementFormat();
		this.aggregationFunction = AggregationFunction.NONE;
		this.hideOnReport = false;
		this.hideRepeating = false;
		this.elementFormats = new HashMap<String,HashMap<String,ElementFormat>>();
		//TODO: do we need summary label here?
	}

	/*
	 * usually this is the column header format
	 */
	@XmlElement(name="header-format")	
	@JsonProperty("headerFormat")
	private ElementFormat headerFormat;

	@XmlElement(name="field-id")
	@JsonProperty("fieldId")
	private String id;
	
	@XmlElement(name="field-name")
	@JsonProperty("fieldName")
	private String name;
	
	@XmlElement(name="field-description")
	@JsonProperty("fieldDescription")
	private String description;

	@XmlElement(name="data-format")
	@JsonProperty("dataFormat")
	private String dataFormat;
	
	@XmlElement(name="formula")
	@JsonProperty("formula")
	private String formula;
		
	@XmlElement(name="null-string")
	@JsonProperty("nullString")
	private String nullString;
	
	@XmlElement(name="hide-repeating")
	@JsonProperty("hideRepeating")
	private boolean hideRepeating;
	
	@XmlElement(name="hide-on-report")
	@JsonProperty("hideOnReport")
	private boolean hideOnReport;
	
	@XmlElement(name="aggregation-function")
	@JsonProperty("aggregationFunction")
	private AggregationFunction aggregationFunction;

    @XmlElement(name="element-formats")
    @JsonProperty("elementFormats")
    
    /*
     * For the real detailfield Format we use the null-key in this map
     *
     * should header format and summary format also go here?
     * "normal" group footer formats too?
     */
	private HashMap<String,HashMap<String,ElementFormat>> elementFormats;
    
    public ElementFormat getHeaderFormat() {
		return headerFormat;
	}

	public void setHeaderFormat(ElementFormat headerFormat) {
		this.headerFormat = headerFormat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHideRepeating() {
		return hideRepeating;
	}

	public void setHideRepeating(boolean hideRepeating) {
		this.hideRepeating = hideRepeating;
	}

	public boolean isHideOnReport() {
		return hideOnReport;
	}
    /*
    When we need two summary fields we create two definitions and make
    one of them invisible
     */
	public void setHideOnReport(boolean hideOnReport) {
		this.hideOnReport = hideOnReport;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
	}

	public String getNullString() {
		return nullString;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setAggregationFunction(AggregationFunction aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}

	public AggregationFunction getAggregationFunction() {
		return aggregationFunction;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
