package com.code.dal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.hibernate.exception.ConstraintViolationException;

import com.code.dal.orm.BaseEntity;
import com.code.exceptions.DatabaseConstraintException;
import com.code.exceptions.DatabaseException;
import com.code.services.log4j.Log4j;

public class CustomSession {
	private Session session;
	private List<BaseEntity> insertList = new ArrayList<BaseEntity>();

	// Package visibility to be used only by Data Access Class.
	CustomSession(Session session) {
		this.session = session;
	}

	// Package visibility to be used only by this package.
	Session getSession() {
		return this.session;
	}

	// -----------------------------------------------------------------------------

	public void beginTransaction() {
		session.beginTransaction();
	}

	public void flushTransaction() {
		session.flush();
	}

	public void commitTransaction() throws DatabaseException {
		try {
			session.getTransaction().commit();
			insertList.clear();
		} catch (ConstraintViolationException e) {
			Log4j.traceErrorException(CustomSession.class, e, "CustomSession");
			throw new DatabaseConstraintException(e.getMessage());
		} catch (OptimisticEntityLockException e) {
			Log4j.traceErrorException(CustomSession.class, e, "CustomSession");
			throw new OptimisticEntityLockException(CustomSession.class, e.getMessage());
		} catch (PersistenceException e) {
			Log4j.traceErrorException(CustomSession.class, e, "CustomSession");
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new DatabaseConstraintException(e.getMessage());
			} else if (e instanceof OptimisticLockException) {
				throw new OptimisticLockException(e.getMessage());
			} else {
				throw new DatabaseException(e.getMessage());
			}
		} catch (Exception e) {
			Log4j.traceErrorException(CustomSession.class, e, "CustomSession");
			throw new DatabaseException(e.getMessage());
		}

	}

	public void rollbackTransaction() {
		session.getTransaction().rollback();
		for (BaseEntity entity : insertList) {
			entity.setId(null);
		}
	}

	public void close() {
		DataAccess.closeInternalSession(session);
	}

	public List<BaseEntity> getInsertList() {
		return insertList;
	}
}