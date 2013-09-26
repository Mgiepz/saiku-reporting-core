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
///**
// * 
// */
//package org.saiku.reporting.core;
//
//import java.awt.Color;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.ArrayList;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.saiku.reporting.core.model.CrosstabLayout;
//import org.saiku.reporting.core.model.ElementFormat;
//import org.saiku.reporting.core.model.FieldDefinition;
//import org.saiku.reporting.core.model.Label;
//import org.saiku.reporting.core.model.ReportSpecification;
//import org.saiku.reporting.core.model.TableDefinition;
//
///**
// * @author mg
// *
// */
//public class Examples {
//	
//	private static final String TEST_XML = "./test-jaxb.xml";
//
//	/**
//	 * @param args
//	 * @throws JAXBException 
//	 * @throws IOException 
//	 */
//	public static void main(String[] args) throws JAXBException, IOException {
//
////			ElementFormat fixture = new ElementFormat();
////			fixture.setFontName("ARIAL");
////			fixture.setFontColor(Color.BLACK);
////			fixture.setFontBold(Boolean.TRUE);
//			
//			ReportSpecification fixture = new ReportSpecification();
//			CrosstabLayout tableDefinition = new CrosstabLayout();
//			ArrayList<FieldDefinition> measures = new ArrayList<FieldDefinition>();
//			measures.add(new FieldDefinition());
//			tableDefinition.setMeasures(measures );
//			fixture.setTableDefinition(tableDefinition);
//			fixture.setReportName("report1");
//			ArrayList<Label> pageFooters = new ArrayList<Label>();
//			Label l = new Label();
//			l.setValue("value $(abc)");
//			l.setFormat(new ElementFormat());
//
//			Label l2 = new Label();
//			l2.setValue("value $(abc)");
//			l2.setFormat(new ElementFormat());
//			
//			pageFooters.add(l);
//			pageFooters.add(l2);
//			fixture.setPageFooters(pageFooters);
//			
//
//
//			// create JAXB context and instantiate marshaller
//			JAXBContext jc = JAXBContext.newInstance(ReportSpecification.class,TableDefinition.class,CrosstabLayout.class);
//			Marshaller m = jc.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			m.marshal(fixture, System.out);
//
//			Writer w = null;
//			try {
//				w = new FileWriter(TEST_XML);
//				m.marshal(fixture, w);
//			} finally {
//				try {
//					w.close();
//				} catch (Exception e) {
//				}
//			}
//
//			// get variables from our xml file, created before
//			System.out.println();
//			System.out.println("Output from our XML File: ");
//			Unmarshaller um = jc.createUnmarshaller();
//			
//			ReportSpecification fixture2 = (ReportSpecification) um.unmarshal(new FileReader(TEST_XML));
//			
//			//json
//		
//			ObjectMapper mapper = new ObjectMapper();
//			String writeValueAsString = mapper.writeValueAsString(fixture);
//			System.out.println(writeValueAsString);
//			
//			ReportSpecification masterModel = mapper.readValue(writeValueAsString, ReportSpecification.class);
//			
//			System.out.println("Table model class= " + masterModel.getTableDefinition().getClass().getCanonicalName());
//			
//		}
//
//}
