package com.code.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRSaver;

public class ReportService extends DataAccess {
	private static final String reportsRoot = InfoSysConfigurationService.getReportsRoot();
	private static final String schemaName = InfoSysConfigurationService.getHibernateDefaultSchema();

	private ReportService() {
	}

	public static byte[] getReportData(final String reportFilePath, final Map<String, Object> parameters) throws Exception {
		final List<byte[]> data = new ArrayList<byte[]>();

		final Session session = sessionFactory.openSession();
		session.doWork(new Work() {
			public void execute(Connection con) throws SQLException {
				try {
					parameters.put("P_REPORTS_ROOT", reportsRoot);
					parameters.put("P_SCHEMA_NAME", schemaName);
					// parameters.put("P_DATABASE_TYPE", DataAccess.databaseEngineName.equals(DatabaseEngineNameEnum.SQL.getCode())? "SQL": DataAccess.databaseEngineName.equals(DatabaseEngineNameEnum.ORACLE.getCode())? "ORACLE": "POSTGRES");
					JasperReport jasperReport = compileReport(reportFilePath);
					compileSubReports(jasperReport);
					data.add(JasperRunManager.runReportToPdf(jasperReport, parameters, con));
				} catch (Exception e) {
					Log4j.traceErrorException(ReportService.class, e, "ReportService");
					throw new SQLException(e.getMessage());
				} catch (Throwable e) {
					Log4j.traceErrorException(ReportService.class, e, "ReportService");
					throw new SQLException(e.getMessage());
				} finally {
					session.close();
				}
			}
		});
		return data.get(0);
	}

	private static JasperReport compileReport(String reportPath) throws JRException {
		return JasperCompileManager.compileReport(reportsRoot + reportPath);
	}

	/**
	 * Recursively compile sub-reports
	 */
	private static JasperReport compileSubReports(JasperReport jasperReport) throws Throwable {
		JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
			@Override
			public void visitSubreport(JRSubreport subreport) {
				try {
					String subReportName = subreport.getExpression().getText().replace(".jasper", ".jrxml");
					subReportName = subReportName.substring(subReportName.lastIndexOf("\"/") + 1, subReportName.length() - 1);

					JasperReport compiledSubRep = compileReport(subReportName);
					JRSaver.saveObject(compiledSubRep, reportsRoot + subReportName.replace(".jrxml", ".jasper"));
					compileSubReports(compiledSubRep);
				} catch (JRException e) {
					Log4j.traceErrorException(ReportService.class, e, "ReportService");
				} catch (Throwable e) {
					Log4j.traceErrorException(ReportService.class, e, "ReportService");
				}

			}

			@Override
			public void visitTextField(JRTextField textField) {
			}

			@Override
			public void visitComponentElement(JRComponentElement componentElement) {
			}

			@Override
			public void visitGenericElement(JRGenericElement element) {
			}

			@Override
			public void visitBreak(JRBreak arg0) {
			}

			@Override
			public void visitChart(JRChart arg0) {
			}

			@Override
			public void visitCrosstab(JRCrosstab arg0) {
			}

			@Override
			public void visitElementGroup(JRElementGroup arg0) {
			}

			@Override
			public void visitEllipse(JREllipse arg0) {
			}

			@Override
			public void visitFrame(JRFrame arg0) {
			}

			@Override
			public void visitImage(JRImage arg0) {
			}

			@Override
			public void visitLine(JRLine arg0) {
			}

			@Override
			public void visitRectangle(JRRectangle arg0) {
			}

			@Override
			public void visitStaticText(JRStaticText arg0) {
			}
		});
		return jasperReport;
	}
}