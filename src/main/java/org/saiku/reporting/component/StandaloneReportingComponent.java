package org.saiku.reporting.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.metadata.ReportProcessTaskRegistry;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfPageableModule;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlTableModule;
import org.pentaho.reporting.engine.classic.core.parameters.DefaultParameterContext;
import org.pentaho.reporting.engine.classic.core.parameters.ParameterContext;
import org.pentaho.reporting.engine.classic.core.parameters.ValidationResult;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.saiku.reporting.component.output.PDFOutput;
import org.saiku.reporting.component.output.PageableHTMLOutput;
import org.saiku.reporting.component.output.ReportOutputHandler;

/**
 * This is a standalone version of the pentaho platform 
 * reporting component
 * 
 * @author mg
 *
 */
public class StandaloneReportingComponent implements IReportingComponent
{

	private static final Log log = LogFactory.getLog(StandaloneReportingComponent.class);

	private String outputType;
	private String outputTarget;
	private String defaultOutputTarget;
	private MasterReport report;
	private Map<String, Object> inputs;
	private OutputStream outputStream;
	private InputStream reportDefinitionInputStream;
	private Serializable fileId;
	private boolean paginateOutput;
	private int acceptedPage;
	private int pageCount;
	private boolean dashboardMode;


	public String getOutputType() {
		return outputType;
	}
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	public String getOutputTarget() {
		return outputTarget;
	}
	public void setOutputTarget(String outputTarget) {
		this.outputTarget = outputTarget;
	}
	public String getDefaultOutputTarget() {
		return defaultOutputTarget;
	}
	public void setDefaultOutputTarget(String defaultOutputTarget) {
		this.defaultOutputTarget = defaultOutputTarget;
	}
	public MasterReport getReport() {
		return report;
	}
	public void setReport(MasterReport report) {
		this.report = report;
	}
	public Map<String, Object> getInputs() {
		return inputs;
	}
	public void setInputs(Map<String, Object> inputs) {
		this.inputs = inputs;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public InputStream getReportDefinitionInputStream() {
		return reportDefinitionInputStream;
	}
	public void setReportDefinitionInputStream(
			InputStream reportDefinitionInputStream) {
		this.reportDefinitionInputStream = reportDefinitionInputStream;
	}
	public Serializable getFileId() {
		return fileId;
	}
	public void setFileId(Serializable fileId) {
		this.fileId = fileId;
	}
	public boolean isPaginateOutput() {
		return paginateOutput;
	}
	public void setPaginateOutput(boolean paginateOutput) {
		this.paginateOutput = paginateOutput;
	}
	public int getAcceptedPage() {
		return acceptedPage;
	}
	public void setAcceptedPage(int acceptedPage) {
		this.acceptedPage = acceptedPage;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public boolean isDashboardMode() {
		return dashboardMode;
	}
	public void setDashboardMode(boolean dashboardMode) {
		this.dashboardMode = dashboardMode;
	}

	/**
	 * This method will determine if the component instance 'is valid.' The validate() is called after all of the bean 'setters' have been called, so we may
	 * validate on the actual values, not just the presence of inputs as we were historically accustomed to.
	 * <p/>
	 * Since we should have a list of all action-sequence inputs, we can determine if we have sufficient inputs to meet the parameter requirements of the
	 * report-definition. This would include validation of values and ranges of values.
	 *
	 * @return true if valid
	 * @throws Exception
	 */
	public boolean validate() throws Exception
	{
		if (reportDefinitionInputStream == null && fileId == null)
		{
			log.error("ReportPlugin.reportDefinitionNotProvided");
			return false;
		}
		if (outputStream == null)
		{
			log.error("ReportPlugin.outputStreamRequired");
			return false;
		}
		if (inputs == null)
		{
			log.error("ReportPlugin.inputParameterRequired");
			return false;
		}
		return true;
	}

	/**
	 * Perform the primary function of this component, this is, to execute. This method will be invoked immediately following a successful validate().
	 *
	 * @return true if successful execution
	 * @throws Exception
	 */
	public boolean execute() throws Exception
	{
		final MasterReport report = getReport();

		try
		{
			final DefaultParameterContext parameterContext = new DefaultParameterContext(report);
			// open parameter context
			final ValidationResult vr = ReportContentUtil.applyInputsToReportParameters(getReport(), parameterContext, inputs, null);

			if (vr.isEmpty() == false)
			{
				return false;
			}
			parameterContext.close();

			final String outputType = computeEffectiveOutputTarget();
			final ReportOutputHandler reportOutputHandler = createOutputHandlerForOutputType(outputType);
			if (reportOutputHandler == null)
			{
				log.warn("ReportPlugin.warnUnprocessableRequest " +  outputType);
				return false;
			}
			synchronized (reportOutputHandler.getReportLock())
			{
				try
				{
					pageCount = reportOutputHandler.generate(report, acceptedPage, outputStream, getYieldRate());
					return pageCount != -1;
				}
				finally
				{
					reportOutputHandler.close();
				}
			}
		}
		catch (Throwable t)
		{
			log.error("ReportPlugin.executionFailed " + t.getMessage());
		}
		// lets not pretend we were successfull, if the export type was not a valid one.
		return false;
	}


	/**
	 * Keep it simple for the moment
	 * @return
	 */
	private String computeEffectiveOutputTarget() {

		final String outputTarget = getOutputTarget();
		if (outputTarget != null)
		{
			if (isValidOutputType(outputTarget) == false)
			{
				log.warn("ReportPlugin.warnInvalidOutputTarget");
			}
			return outputTarget;
		}
		return null;
	}

	private boolean isValidOutputType(final String outputType)
	{
		if (PNG_EXPORT_TYPE.equals(outputType))
		{
			return true;
		}
		return ReportProcessTaskRegistry.getInstance().isExportTypeRegistered(outputType);
	}

	protected int getYieldRate()
	{
		final Object yieldRate = getInput(REPORTGENERATE_YIELDRATE, null);
		if (yieldRate instanceof Number)
		{
			final Number n = (Number) yieldRate;
			if (n.intValue() < 1)
			{
				return 0;
			}
			return n.intValue();
		}
		return 0;
	}

	protected Object getInput(final String key, final Object defaultValue)
	{
		if (inputs != null)
		{
			final Object input = inputs.get(key);
			if (input != null)
			{
				return input;
			}
		}
		return defaultValue;
	}

	protected ReportOutputHandler createOutputHandlerForOutputType(final String outputType) throws IOException
	{
		//TODO: For now we dont cache the outputhandler  
		final ReportOutputHandler reportOutputHandler;

		if (HtmlTableModule.TABLE_HTML_PAGE_EXPORT_TYPE.equals(outputType))
		{
			if (dashboardMode)
			{
				report.getReportConfiguration().setConfigProperty(HtmlTableModule.BODY_FRAGMENT, "true");
			}
			// don't use the content repository
			final Configuration globalConfig = ClassicEngineBoot.getInstance().getGlobalConfig();

			//TODO: for now we dont need url rewriting, only when we use images that are in temp folder
			String contentHandlerPattern = null;

			contentHandlerPattern += (String) getInput(REPORTHTML_CONTENTHANDLER_PATTERN,
					globalConfig.getConfigProperty("org.pentaho.web.ContentHandler")); //$NON-NLS-1$
			reportOutputHandler = new PageableHTMLOutput(contentHandlerPattern);

		}
		else if (PdfPageableModule.PDF_EXPORT_TYPE.equals(outputType)){
			reportOutputHandler = new PDFOutput();
		}
		else{
			return null;
		}
		//return cache.put(reportCacheKey, reportOutputHandler); <- implement this!
		return reportOutputHandler;
	}

	/**
	 * Static initializer block to guarantee that the ReportingComponent will be in a state where the reporting engine will be booted. We have a system listener
	 * which will boot the reporting engine as well, but we do not want to solely rely on users having this setup correctly. The errors you receive if the engine
	 * is not booted are not very helpful, especially to outsiders, so we are trying to provide multiple paths to success. Enjoy.
	 */
	static
	{
		ClassicEngineBoot.getInstance().start();
	}

	@Override
	public void setForceDefaultOutputTarget(boolean forceDefaultOutputTarget) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isForceDefaultOutputTarget() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Serializable getReportFileId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setReportFileId(Serializable fileId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isPrint() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setPrint(boolean print) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getPrinter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPrinter(String printer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getComputedOutputTarget() throws IOException,
			ResourceException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void applyInputsToReportParameters(MasterReport report,
			ParameterContext context) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Deprecated
	public ValidationResult applyInputsToReportParameters(
			ParameterContext context, ValidationResult validationResult)
			throws IOException, ResourceException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean outputSupportsPagination() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int paginate() throws IOException, ResourceException {
		// TODO Auto-generated method stub
		return 0;
	}

}
