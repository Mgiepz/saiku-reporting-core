package org.saiku.reporting.core.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name="report")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportSpecification {

	public HashMap<String, String> getCustomValues() {
		return customValues;
	}

	public void setCustomValues(HashMap<String, String> customValues) {
		this.customValues = customValues;
	}

	public ArrayList<Chart> getCharts() {
		return charts;
	}

	public void setCharts(ArrayList<Chart> charts) {
		this.charts = charts;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public ReportSpecification() {
		this.reportName = "Report";
		this.reportHeaders = new ArrayList<Label>();
		this.pageHeaders = new ArrayList<Label>();
		this.pageFooters = new ArrayList<Label>();
		this.reportFooters = new ArrayList<Label>();
		this.pageSetup = new PageSetup();
		this.charts = new ArrayList<Chart>();
	}

	@XmlElement(name="report-name")
	@JsonProperty("reportName")
	private String reportName;
	
	@XmlElementWrapper(name="report-headers")
	@XmlElement(name="label")
	@JsonProperty("reportHeaders")
	private ArrayList<Label> reportHeaders;
	
	@XmlElementWrapper(name="report-footers")
	@XmlElement(name="label")
	@JsonProperty("reportFooters")
	private ArrayList<Label> reportFooters;
	
	@XmlElementWrapper(name="page-headers")
	@XmlElement(name="label")
	@JsonProperty("pageHeaders")
	private ArrayList<Label> pageHeaders;
	
	@XmlElementWrapper(name="page-footers")
	@XmlElement(name="label")
	@JsonProperty("pageFooters")
	private ArrayList<Label> pageFooters;

    @XmlElementWrapper(name="groupDefinitions")
    @XmlElement(name="group")
    @JsonProperty("groupDefinitions")
    private ArrayList<GroupDefinition> groupDefinitions;

    @XmlElementWrapper(name="fieldDefinitions")
    @XmlElement(name="field")
    @JsonProperty("fieldDefinitions")
    private ArrayList<FieldDefinition> fieldDefinitions;

	@XmlElementWrapper(name="charts")
	@XmlElement(name="chart")
	@JsonProperty("charts")
	private ArrayList<Chart> charts;
	
	@XmlElement(name="page-setup")
	@JsonProperty("pageSetup")
	private PageSetup pageSetup;

	@XmlElement(name="data-source")
	@JsonProperty("dataSource")
	private DataSource dataSource;

	@XmlElement(name="parameters")
	@JsonProperty("parameters")
	private ArrayList<Parameter> parameters;

	@XmlElement(name="custom-values")
	@JsonProperty("customValues")
	private HashMap<String,String> customValues;
	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public ArrayList<Label> getReportHeaders() {
		return reportHeaders;
	}

	public void setReportHeaders(ArrayList<Label> reportHeaders) {
		this.reportHeaders = reportHeaders;
	}

	public ArrayList<Label> getReportFooters() {
		return reportFooters;
	}

	public void setReportFooters(ArrayList<Label> reportFooters) {
		this.reportFooters = reportFooters;
	}

	public ArrayList<Label> getPageHeaders() {
		return pageHeaders;
	}

	public void setPageHeaders(ArrayList<Label> pageHeaders) {
		this.pageHeaders = pageHeaders;
	}

	public ArrayList<Label> getPageFooters() {
		return pageFooters;
	}

	public void setPageFooters(ArrayList<Label> pageFooters) {
		this.pageFooters = pageFooters;
	}

	public PageSetup getPageSetup() {
		return pageSetup;
	}

	public void setPageSetup(PageSetup pageSetup) {
		this.pageSetup = pageSetup;
	}

	public void setChart(ArrayList<Chart> charts) {
		this.charts = charts;
	}

	public ArrayList<Chart> getChart() {
		return charts;
	}

	public ArrayList<GroupDefinition> getGroupDefinitions() {
		return groupDefinitions;
	}

	public void setGroupDefinitions(ArrayList<GroupDefinition> groupDefinitions) {
		this.groupDefinitions = groupDefinitions;
	}

	public ArrayList<FieldDefinition> getFieldDefinitions() {
		return fieldDefinitions;
	}

	public void setFieldDefinitions(ArrayList<FieldDefinition> fieldDefinitions) {
		this.fieldDefinitions = fieldDefinitions;
	}

}
