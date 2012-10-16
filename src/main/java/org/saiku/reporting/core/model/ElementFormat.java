package org.saiku.reporting.core.model;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonProperty;
import org.pentaho.reporting.engine.classic.core.ElementAlignment;
import org.saiku.reporting.core.adapter.ColorAdapter;
import org.saiku.reporting.core.adapter.HorizontalElementAlignmentAdapter;
import org.saiku.reporting.core.adapter.VerticalElementAlignmentAdapter;


@XmlRootElement(name="element-format")
@XmlAccessorType(XmlAccessType.FIELD)
public class ElementFormat extends BaseElement {
	
	public ElementFormat() {
//		this.horizontalAlignment = ElementAlignment.CENTER;
//		this.verticalAlignment = ElementAlignment.MIDDLE;		
		this.width = new Length(LengthUnit.PERCENTAGE, Math.round(33*1000)/1000);

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * this acts as a means to override the field with a message string
	 */
	@XmlElement(name="label")
	@JsonProperty("label")
	private String label;	
	
	@XmlElement(name="font-name")
	@JsonProperty("fontName")
	private String fontName;

	@XmlElement(name="font-bold")
	@JsonProperty("fontBold")
	private Boolean fontBold;
	
	@XmlElement(name="font-italic")
	@JsonProperty("fontItalic")
	private Boolean fontItalic;
	
	@XmlElement(name="font-underline")
	@JsonProperty("fontUnderline")
	private Boolean fontUnderline;
	
	@XmlElement(name="font-strikethrough")
	@JsonProperty("fontStrikethrough")
	private Boolean fontStrikethrough;
	
	@XmlElement(name="font-size")
	@JsonProperty("fontSize")
	private Integer fontSize;
	
	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlElement(name="font-color")
	@JsonProperty("fontColor")
	private Color fontColor;

	@XmlJavaTypeAdapter(ColorAdapter.class)
	@XmlElement(name="background-color")
	@JsonProperty("backgroundColor")
	private Color backgroundColor;
	
	@XmlElement(name="width")
	@JsonProperty("width")
	private Length width;
	
	@XmlElement(name="left-padding")
	@JsonProperty("leftPadding")
	private Float leftPadding;
	
	@XmlElement(name="right-padding")
	@JsonProperty("rightPadding")
	private Float rightPadding;

	@XmlJavaTypeAdapter(HorizontalElementAlignmentAdapter.class)
	@XmlElement(name="horizontal-alignment")
	@JsonProperty("horizontalAlignment")
	private ElementAlignment horizontalAlignment;
	
	@XmlJavaTypeAdapter(VerticalElementAlignmentAdapter.class)
	@XmlElement(name="vertical-alignment")
	@JsonProperty("verticalAlignment")
	private ElementAlignment verticalAlignment;

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public Boolean getFontBold() {
		return fontBold;
	}

	public void setFontBold(Boolean fontBold) {
		this.fontBold = fontBold;
	}

	public Boolean getFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(Boolean fontItalic) {
		this.fontItalic = fontItalic;
	}

	public Boolean getFontUnderline() {
		return fontUnderline;
	}

	public void setFontUnderline(Boolean fontUnderline) {
		this.fontUnderline = fontUnderline;
	}

	public Boolean getFontStrikethrough() {
		return fontStrikethrough;
	}

	public void setFontStrikethrough(Boolean fontStrikethrough) {
		this.fontStrikethrough = fontStrikethrough;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Color getFontColor() {
		return fontColor;
	}

	
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Length getWidth() {
		return width;
	}

	public void setWidth(Length width) {
		this.width = width;
	}

	public Float getLeftPadding() {
		return leftPadding;
	}

	public void setLeftPadding(Float leftPadding) {
		this.leftPadding = leftPadding;
	}

	public Float getRightPadding() {
		return rightPadding;
	}

	public void setRightPadding(Float rightPadding) {
		this.rightPadding = rightPadding;
	}

	public ElementAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(ElementAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public ElementAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(ElementAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

}
