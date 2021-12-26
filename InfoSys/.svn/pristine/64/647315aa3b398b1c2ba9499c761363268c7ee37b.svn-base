package com.code.ui.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.code.dal.DataAccess;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.util.CommonService;

public class AppContextListener implements ServletContextListener {
	// private JMSFishingQueueReceive fishingQueueReceive;
	// private JMSHCMQueueReceive jmsHCMQueueReceive;

	/**
	 * Context Initializer
	 */
	public void contextInitialized(ServletContextEvent event) {
		boolean isProduction = Boolean.valueOf(DataAccess.configuration.getString("datasourceFlag"));

		DataAccess.init(isProduction, event.getServletContext());
		InfoSysConfigurationService.init();
		// fishingQueueReceive = new JMSFishingQueueReceive();
		// jmsHCMQueueReceive = new JMSHCMQueueReceive();
		// try {
		// fishingQueueReceive.init();
		// } catch (Exception e) {
		// Log4j.traceError(AppContextListener.class, "Failed to start Fishing Queue JMS Server");
		// Log4j.traceErrorException(AppContextListener.class, e, "AppContextListener");
		// }
		// try {
		// jmsHCMQueueReceive.init();
		// } catch (Exception e) {
		// Log4j.traceError(AppContextListener.class, "Failed to start HCM Queue JMS Server");
		// Log4j.traceErrorException(AppContextListener.class, e, "AppContextListener");
		// }

		CommonService.runThreads();
		CommonService.startYaqeenJob();
		CommonService.startConversationReminderJob();
		CommonService.startDelayedResultsRemiderJob();
	}

	/**
	 * Halt all threads when context is destroyed
	 */
	public void contextDestroyed(ServletContextEvent event) {
		CommonService.stopThreads();
		DataAccess.closeSessionFactory();
		// try {
		// if (fishingQueueReceive != null) {
		// fishingQueueReceive.close();
		// }
		// } catch (JMSException e) {
		// Log4j.traceError(AppContextListener.class, "Failed to stop Fishing Queue JMS Server");
		// Log4j.traceErrorException(AppContextListener.class, e, "AppContextListener");
		// }
		//
		// try {
		// if (jmsHCMQueueReceive != null) {
		// jmsHCMQueueReceive.close();
		// }
		// } catch (JMSException e) {
		// Log4j.traceError(AppContextListener.class, "Failed to stop HCM Queue JMS Server");
		// Log4j.traceErrorException(AppContextListener.class, e, "AppContextListener");
		// }
	}
}