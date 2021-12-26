package com.code.dal.orm.audit;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.orm.BaseEntity;

@NamedQueries({
	   @NamedQuery(name  = "thread_getThread", 
			   	   query = " select t " +
			   			   " from RunningThread t " +
			   			   " where (:P_THREAD_NAME = '-1' or t.threadName = :P_THREAD_NAME) " +
			   			   " order by t.id"
	   )
})

@Entity
@Table(name = "RUNNING_THREADS")
public class RunningThread extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3855417276065047275L;
	private Long id;
	private String threadName;
	private Long runningTime;
	

	public RunningThread(){}
	
	public RunningThread(String threadName, long runningTime){
		this.threadName = threadName;
		this.runningTime = runningTime;
	}
	
	@SequenceGenerator(name = "ThreadSeq", sequenceName = "THREADS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ThreadSeq")
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long auditLogId) {
		this.id = auditLogId;
	}

	@Basic
	@Column(name = "THREAD_NAME")
	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	@Basic
	@Column(name = "RUNNING_TIME_MILISECONDS")
	public Long getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(Long runningTime) {
		this.runningTime = runningTime;
	}
}
