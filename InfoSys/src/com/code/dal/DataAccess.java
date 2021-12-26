package com.code.dal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletContext;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONException;
import org.json.JSONObject;

import com.code.dal.audit.DeleteableAuditEntity;
import com.code.dal.audit.InsertableAuditEntity;
import com.code.dal.audit.UpdateableAuditEntity;
import com.code.dal.orm.AuditEntity;
import com.code.dal.orm.BaseEntity;
import com.code.dal.orm.audit.AuditLog;
import com.code.enums.AuditOperationsEnum;
import com.code.enums.FlagsEnum;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.exceptions.NoDataException;
import com.code.services.log4j.Log4j;

public class DataAccess {
	protected static SessionFactory sessionFactory;
	protected static final Semaphore DB_CONNECTION_SEMAPHORE = new Semaphore(100, true);
	public static ResourceBundle configuration = ResourceBundle.getBundle("com.code.resources.config");
	public static String databaseEngineName;

	protected DataAccess() {
	}

	public static void init(boolean isProduction, ServletContext sc) {
		try {
			if (sessionFactory == null) {
				Configuration annotationConfiguration = new CustomeAnnotationConfigurations(sc).configure("com/code/dal/hibernate.cfg.xml").addPackage("com.code.dal.orm");
				if (isProduction) {
					annotationConfiguration.setProperty("hibernate.connection.datasource", configuration.getString("hibernate.connection.datasource"));
					annotationConfiguration.setProperty("hibernate.transaction.factory_class", configuration.getString("hibernate.transaction.factory_class"));
					annotationConfiguration.setProperty("hibernate.transaction.manager_lookup_class", configuration.getString("hibernate.transaction.manager_lookup_class"));
				
				} else {
					annotationConfiguration.setProperty("hibernate.connection.driver_class", configuration.getString("hibernate.connection.driver_class"));
					annotationConfiguration.setProperty("hibernate.connection.url", configuration.getString("hibernate.connection.url"));
					annotationConfiguration.setProperty("hibernate.connection.username", configuration.getString("hibernate.connection.username"));
					annotationConfiguration.setProperty("hibernate.connection.password", configuration.getString("hibernate.connection.password"));
					annotationConfiguration.setProperty("hibernate.connection.pool_size", configuration.getString("hibernate.connection.pool_size"));
					annotationConfiguration.setProperty("hibernate.show_sql", configuration.getString("hibernate.show_sql"));
				}

				annotationConfiguration.setProperty("hibernate.dialect", configuration.getString("hibernate.dialect"));
				annotationConfiguration.setProperty("hibernate.default_schema", configuration.getString("hibernate.default_schema"));
				annotationConfiguration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("com/code/dal/hibernate.cfg.xml").applySettings(annotationConfiguration.getProperties()).build();
				sessionFactory = annotationConfiguration.buildSessionFactory(serviceRegistry);
				getDatabaseProductName();
			}
		} catch (HibernateException exception) {
			System.out.println("Problem creating session Factory!");
			Log4j.traceErrorException(DataAccess.class, exception, "DataAccess");
		}

	}

	private static Session openInternalSession() {
		DB_CONNECTION_SEMAPHORE.acquireUninterruptibly();
		return sessionFactory.openSession();
	}

	public static void closeInternalSession(Session session) {
		session.close();
		DB_CONNECTION_SEMAPHORE.release();
	}

	public static CustomSession getSession() {
		DB_CONNECTION_SEMAPHORE.acquireUninterruptibly();
		return new CustomSession(sessionFactory.openSession());
	}

	public static void addEntity(BaseEntity bean, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = false;
		if (useSession != null && useSession.length > 0)
			isOpenedSession = true;

		Session session = isOpenedSession ? useSession[0].getSession() : openInternalSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			if (isOpenedSession) {
				useSession[0].getInsertList().add(bean);
			}

			session.save(bean);
			audit(bean, AuditOperationsEnum.INSERT, session);

			if (!isOpenedSession)
				session.getTransaction().commit();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.getTransaction().rollback();
			if (e instanceof ConstraintViolationException)
				throw new DatabaseConstraintException(e.getMessage());
			else
				throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				closeInternalSession(session);
		}
	}

	public static void updateEntity(BaseEntity bean, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = false;
		if (useSession != null && useSession.length > 0)
			isOpenedSession = true;

		Session session = isOpenedSession ? useSession[0].getSession() : openInternalSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			session.saveOrUpdate(bean);
			audit(bean, AuditOperationsEnum.UPDATE, session);

			if (!isOpenedSession)
				session.getTransaction().commit();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.getTransaction().rollback();
			if (e instanceof ConstraintViolationException)
				throw new DatabaseConstraintException(e.getMessage());
			else
				throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				closeInternalSession(session);
		}
	}

	public static void deleteEntity(BaseEntity bean, CustomSession... useSession) throws DatabaseException {
		boolean isOpenedSession = false;
		if (useSession != null && useSession.length > 0)
			isOpenedSession = true;

		Session session = isOpenedSession ? useSession[0].getSession() : openInternalSession();

		try {
			if (!isOpenedSession)
				session.beginTransaction();

			session.delete(bean);
			audit(bean, AuditOperationsEnum.DELETE, session);

			if (!isOpenedSession)
				session.getTransaction().commit();
		} catch (Exception e) {
			if (!isOpenedSession)
				session.getTransaction().rollback();
			if (e instanceof ConstraintViolationException)
				throw new DatabaseConstraintException(e.getMessage());
			else
				throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				closeInternalSession(session);
		}
	}

	public static <T> List<T> executeNamedQuery(Class<T> dataClass, String queryName, Map<String, Object> parameters, CustomSession... useSession) throws DatabaseException, NoDataException {
		return executeUpdateNamedQuery(dataClass, queryName, parameters, false, useSession);
	}

	public static <T> List<T> updateDeleteNamedQuery(Class<T> dataClass, String queryName, Map<String, Object> parameters, CustomSession useSession) throws DatabaseException, NoDataException {
		return executeUpdateNamedQuery(dataClass, queryName, parameters, true, useSession);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> executeUpdateNamedQuery(Class<T> dataClass, String queryName, Map<String, Object> parameters, boolean update, CustomSession... useSession) throws DatabaseException, NoDataException {
		boolean isOpenedSession = false;
		if (useSession != null && useSession.length > 0)
			isOpenedSession = true;

		Session session = isOpenedSession ? useSession[0].getSession() : openInternalSession();

		try {
			String newQuery = session.getNamedQuery(queryName).getQueryString();
			Query<T> q = session.createQuery(newQuery);
			
			//rownum is not known in MS SQL .... Need for testing
			if(null!= parameters && null != parameters.get("P_OPTIMIZE")&&! parameters.get("P_OPTIMIZE").equals(FlagsEnum.ALL.getCode()+"")) {
				q.setMaxResults(500);
			parameters.remove("P_OPTIMIZE");}
			

			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					Object value = parameters.get(paramName);
					if (value instanceof Integer)
						q.setParameter(paramName, value, org.hibernate.type.IntegerType.INSTANCE);
					else if (value instanceof String)
						q.setParameter(paramName, value, org.hibernate.type.StringType.INSTANCE);
					else if (value instanceof Long)
						q.setParameter(paramName, value, org.hibernate.type.LongType.INSTANCE);
					else if (value instanceof Float)
						q.setParameter(paramName, value, org.hibernate.type.FloatType.INSTANCE);
					else if (value instanceof Double)
						q.setParameter(paramName, value, org.hibernate.type.DoubleType.INSTANCE);
					else if (value instanceof Date)
						q.setParameter(paramName, value, org.hibernate.type.DateType.INSTANCE);
					else if (value instanceof Boolean)
						q.setParameter(paramName, value, org.hibernate.type.BooleanType.INSTANCE);
					else if (value instanceof Integer[])
						q.setParameterList(paramName, (Integer[]) value, org.hibernate.type.IntegerType.INSTANCE);
					else if (value instanceof String[])
						q.setParameterList(paramName, (String[]) value, org.hibernate.type.StringType.INSTANCE);
					else if (value instanceof Long[])
						q.setParameterList(paramName, (Long[]) value, org.hibernate.type.LongType.INSTANCE);
					else if (value instanceof Object[])
						q.setParameterList(paramName, (Object[]) value, org.hibernate.type.ObjectType.INSTANCE);
				}
			}

			List<T> result = new ArrayList<T>();
			if (!update) {
				result = q.list();
			} else {
				List<Integer> resultInteger = new ArrayList<Integer>();
				resultInteger.add(q.executeUpdate());
				result = (List<T>) resultInteger;
			}

			if (result == null || result.size() == 0)
				throw new NoDataException("");

			return result;
		} catch (NoDataException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			if (!isOpenedSession)
				closeInternalSession(session);
		}
	}

	public static <T> List<T> executeNativeQuery(String queryString, Map<String, Object> parameters) throws DatabaseException, NoDataException {
		Session session = openInternalSession();
		Query query = session.createQuery(queryString);

		try {
			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					Object value = parameters.get(paramName);

					if (value instanceof Integer)
						query.setInteger(paramName, (Integer) value);
					else if (value instanceof String)
						query.setString(paramName, (String) value);
					else if (value instanceof Long)
						query.setLong(paramName, (Long) value);
					else if (value instanceof Float)
						query.setFloat(paramName, (Float) value);
					else if (value instanceof Double)
						query.setDouble(paramName, (Double) value);
					else if (value instanceof Date)
						query.setDate(paramName, (Date) value);
					else if (value instanceof Object[])
						query.setParameterList(paramName, (Object[]) value);
				}
			}

			@SuppressWarnings("unchecked")
			List<T> result = query.list();

			if (result == null || result.size() == 0)
				throw new NoDataException("");

			return result;
		} catch (NoDataException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			closeInternalSession(session);
		}
	}
	
	public static <T> List<T> executeHQLQuery(String queryString, Map<String, Object> parameters) throws DatabaseException {
		Session session = openInternalSession();
		Query query = session.createQuery(queryString);

		try {
		    if (parameters != null) {
			for (String paramName : parameters.keySet()) {
			    Object value = parameters.get(paramName);

			    if (value instanceof Integer)
				query.setInteger(paramName, (Integer) value);
			    else if (value instanceof String)
				query.setString(paramName, (String) value);
			    else if (value instanceof Long)
				query.setLong(paramName, (Long) value);
			    else if (value instanceof Float)
				query.setFloat(paramName, (Float) value);
			    else if (value instanceof Double)
				query.setDouble(paramName, (Double) value);
			    else if (value instanceof Date)
				query.setDate(paramName, (Date) value);
			    else if (value instanceof Object[])
				query.setParameterList(paramName, (Object[]) value);
			}
		    }

		    @SuppressWarnings("unchecked")
		    List<T> result = query.list();

		    if (result == null || result.size() == 0)
			return new ArrayList<>();

		    return result;
		} catch (Exception e) {
		    throw new DatabaseException(e.getMessage());
		} finally {
		    closeInternalSession(session);
		}
	    }
	
	private static void audit(BaseEntity bean, AuditOperationsEnum operation, Session session) {
		if ((AuditOperationsEnum.INSERT.equals(operation) && bean instanceof InsertableAuditEntity) || (AuditOperationsEnum.UPDATE.equals(operation) && bean instanceof UpdateableAuditEntity) || (AuditOperationsEnum.DELETE.equals(operation) && bean instanceof DeleteableAuditEntity)) {

			// It is intended not to check for the cast exception here to
			// announce the wrong usage.
			AuditEntity auditableBean = (AuditEntity) bean;

			// If the system user has a value, this means that this transaction
			// will audit this entity.
			if (auditableBean.getSystemUser() != null) {
				AuditLog log = new AuditLog();
				log.setSystemUser(auditableBean.getSystemUser());
				log.setOperation(operation.toString());
				log.setOperationDate(new Date());
				log.setContentEntity(auditableBean.getClass().getCanonicalName());
				log.setContentId(auditableBean.calculateContentId());
				log.setContent(calculateContent(auditableBean).toString());
				session.save(log);
			}
		}
	}

	private static JSONObject calculateContent(AuditEntity orm) {
		JSONObject content = new JSONObject();
		Class<? extends AuditEntity> cls = orm.getClass();
		try {
			Method[] methods = cls.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
					content.put(method.getName(), method.invoke(orm));
				}
			}
		} catch (IllegalArgumentException e) {
			Log4j.traceErrorException(DataAccess.class, e, "DataAccess");
		} catch (JSONException e) {
			Log4j.traceErrorException(DataAccess.class, e, "DataAccess");
		} catch (IllegalAccessException e) {
			Log4j.traceErrorException(DataAccess.class, e, "DataAccess");
		} catch (InvocationTargetException e) {
			Log4j.traceErrorException(DataAccess.class, e, "DataAccess");
		}
		return content;
	}

	public static void closeSessionFactory() {
		sessionFactory.close();
	}
	private static void getDatabaseProductName() {

		final Session session = sessionFactory.openSession();

		session.doWork(new Work() {

			public void execute(Connection connection) throws SQLException {
				try {
					DatabaseMetaData meta = connection.getMetaData();
					databaseEngineName = meta.getDatabaseProductName();

				} finally {
					session.close();
				}
			}
		});
	}
}