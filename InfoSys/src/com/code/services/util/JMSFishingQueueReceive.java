package com.code.services.util;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.code.enums.JMSConfigEnum;
import com.code.services.config.InfoSysConfigurationService;
import com.code.services.log4j.Log4j;
import com.code.services.workflow.surveillance.SurveillanceWorkFlow;

/**
 * This example shows how to establish a connection to and receive messages from a JMS queue. The classes in this package operate on the same JMS queue. Run the classes together to witness messages being sent and received, and to browse the queue for messages. This class is used to receive and remove messages from the queue.
 *
 * @author Copyright (c) 1999-2005 by BEA Systems, Inc. All Rights Reserved.
 */
public class JMSFishingQueueReceive implements MessageListener {
	// Defines the JMS server URL
	public final static String SERVER = InfoSysConfigurationService.getJMSServerURL(); // t3://jbevans-lx.de.oracle.com:8001
	private QueueConnection qConn;
	private QueueSession qSession;
	private QueueReceiver qReceiver;

	/**
	 * Message listener interface.
	 * 
	 * @param msg
	 *            message
	 */
	public void onMessage(Message msg) {
		try {
			Log4j.traceInfo(JMSFishingQueueReceive.class, "OPR FSH MESSAGE : " + msg);
			String msgText;
			if (msg instanceof TextMessage) {
				msgText = ((TextMessage) msg).getText();
			} else {
				msgText = msg.toString();
			}
			Log4j.traceInfo(JMSFishingQueueReceive.class, "OPR FSH MESSAGE : " + msgText);
			SurveillanceWorkFlow.sendNotifications(msgText);
		} catch (JMSException e) {
			Log4j.traceErrorException(JMSFishingQueueReceive.class, e, "JMSFishingQueueReceive");
		}
	}

	/**
	 * Creates all the necessary objects for receiving messages from a JMS queue.
	 * 
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void init() throws NamingException, JMSException {
		InitialContext ctx = getInitialContext();
		QueueConnectionFactory qconFactory = (QueueConnectionFactory) ctx.lookup(JMSConfigEnum.JMS_FACTORY.getCode());
		// Make a connection to the queue
		qConn = qconFactory.createQueueConnection();
		qSession = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		// LookUp FIHSING_AND_EXCURSIONS_QUEUE Queue and make it a receiver queue
		Queue queue = (Queue) ctx.lookup(JMSConfigEnum.FIHSING_AND_EXCURSIONS_QUEUE.getCode());
		qReceiver = qSession.createReceiver(queue);
		qReceiver.setMessageListener(this);
		// Start Connection
		qConn.start();
	}

	/**
	 * Closes JMS objects.
	 * 
	 * @exception JMSException
	 *                if JMS fails to close objects due to internal error
	 */
	public void close() throws JMSException {
		if (qReceiver != null)
			qReceiver.close();
		if (qSession != null)
			qSession.close();
		if (qConn != null)
			qConn.close();
	}

	/**
	 * 
	 * @return
	 * @throws NamingException
	 */
	private static InitialContext getInitialContext() throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JMSConfigEnum.JNDI_FACTORY.getCode());
		env.put(Context.PROVIDER_URL, SERVER);
		return new InitialContext(env);
	}
}