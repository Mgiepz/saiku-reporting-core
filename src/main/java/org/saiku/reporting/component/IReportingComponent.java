package org.saiku.reporting.component;

public interface IReportingComponent {

}





//private void generateHtmlReport(MasterReport output, OutputStream stream,
//		Map<String, Object> reportParameters, HtmlReport report, Integer acceptedPage) throws Exception{
//
//	final SimpleReportingComponent pentahoReportingPlugin = prptProvider.getReportingComponent();
//	pentahoReportingPlugin.setReport(output);
//	pentahoReportingPlugin.setPaginateOutput(true);
//	pentahoReportingPlugin.setInputs(reportParameters);
//	pentahoReportingPlugin.setDefaultOutputTarget(HtmlTableModule.TABLE_HTML_PAGE_EXPORT_TYPE);
//	pentahoReportingPlugin.setOutputTarget(HtmlTableModule.TABLE_HTML_PAGE_EXPORT_TYPE);
//	pentahoReportingPlugin.setDashboardMode(true);
//	pentahoReportingPlugin.setOutputStream(stream);
//	pentahoReportingPlugin.setAcceptedPage(acceptedPage);
//	pentahoReportingPlugin.validate();
//	pentahoReportingPlugin.execute();
//
//	report.setCurrentPage(pentahoReportingPlugin.getAcceptedPage());
//	report.setPageCount(pentahoReportingPlugin.getPageCount());
//
//}