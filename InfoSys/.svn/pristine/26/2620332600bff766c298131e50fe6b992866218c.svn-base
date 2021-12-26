package com.code.ui.backings.base;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstance;
import com.code.dal.orm.workflow.WFTask;
import com.code.dal.orm.workflow.WFTaskData;
import com.code.enums.WFTaskRolesEnum;
import com.code.services.log4j.Log4j;
import com.code.services.setup.EmployeeService;
import com.code.services.workflow.BaseWorkFlow;

@SuppressWarnings("serial")
public abstract class WFBaseBacking extends BaseBacking implements Serializable {
    protected EmployeeData requester;    
    protected EmployeeData currentEmployee;
    
    protected EmployeeData beneficiary;
    protected String beneficiarySearchId;
    
    protected long processId;  // Will be determined always in the sub classes.
    protected WFInstance instance;
    protected WFTask currentTask;
    protected List<WFTaskData> prevTasks;
    protected String taskUrl; // will be used only in the requester role.    
    protected String role;
    
    protected String attachments;
    protected String notificationMessage;
    
    protected Integer instanceApproved = null;
    
    protected void init(){
        super.init();
        HttpServletRequest req = getRequest();
        
        try{
            if(req.getParameter("taskId") == null){
                // Requster Role.
                role = WFTaskRolesEnum.REQUESTER.getCode();
                
                requester = super.loginEmpData;
                currentEmployee = null;
                beneficiary = null;
                beneficiarySearchId = null;
                instance = null;
                currentTask = null;
                prevTasks = null;
                attachments = null;
                notificationMessage = null;
                
                taskUrl = getCompleteURL(req).replace("openedNode=1000", "openedNode=1000000"); // the replace is for menu handling.
                taskUrl = taskUrl.substring(taskUrl.indexOf("/", 1), taskUrl.length());
            }
            else{
                currentTask = BaseWorkFlow.getWFTaskById(Long.parseLong(req.getParameter("taskId")));
                instance = BaseWorkFlow.getWFInstanceById(currentTask.getInstanceId());
                attachments = instance.getAttachments();
                
                role = currentTask.getAssigneeWfRole();
                
                
                requester = EmployeeService.getEmployee(instance.getRequesterId());
                currentEmployee = EmployeeService.getEmployee(currentTask.getOriginalId());
                
                prevTasks = BaseWorkFlow.getWFInstanceCompletedTasksData(currentTask.getInstanceId(), currentTask.getTaskId(), currentTask.getLevel());
                
//                if(role.equals(WFTaskRolesEnum.NOTIFICATION.getCode())){
//                    String prevNotifyTaskAction = prevTasks.get(prevTasks.size()-1).getAction();
//                    if(prevNotifyTaskAction.equals(WFTaskActionsEnum.REJECT.getCode())) {
//                        notificationMessage = this.getParameterizedMessage("notify_requestRejected");
//                        instanceApproved = new Integer(FlagsEnum.OFF.getCode());
//                    }
//                    else if(prevNotifyTaskAction.equals(WFTaskActionsEnum.APPROVE.getCode()) ||
//                            prevNotifyTaskAction.equals(WFTaskActionsEnum.SUPER_SIGN.getCode()) ||
//                            prevNotifyTaskAction.equals(WFTaskActionsEnum.DATA_ENTRY.getCode())) {
//                        notificationMessage = this.getParameterizedMessage("notify_requestApproved");
//                        instanceApproved = new Integer(FlagsEnum.ON.getCode());
//                    }
//                    else
//                        notificationMessage = null;
//                }
                  
                if(currentTask.getAction() != null){
                    this.role = WFTaskRolesEnum.HISTORY.getCode();
                    prevTasks.add(BaseWorkFlow.getWFTaskDataById(currentTask.getTaskId()));
                }
            }
        }
        catch (Exception e) {
			 Log4j.traceErrorException(WFBaseBacking.class, e, "WFBaseBacking");
			 this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
    }

    public void setRequester(EmployeeData requester) {
        this.requester = requester;
    }

    public EmployeeData getRequester() {
        return requester;
    }

    public void setCurrentEmployee(EmployeeData currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public EmployeeData getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentTask(WFTask currentTask) {
        this.currentTask = currentTask;
    }

    public WFTask getCurrentTask() {
        return currentTask;
    }

    public void setPrevTasks(List<WFTaskData> prevTasks) {
        this.prevTasks = prevTasks;
    }

    public List<WFTaskData> getPrevTasks() {
        return prevTasks;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setBeneficiary(EmployeeData beneficiary) {
        this.beneficiary = beneficiary;
    }

    public EmployeeData getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiarySearchId(String beneficiarySearchId) {
        this.beneficiarySearchId = beneficiarySearchId;
    }

    public String getBeneficiarySearchId() {
        return beneficiarySearchId;
    }

	public Integer getInstanceApproved() {
		return instanceApproved;
	}

	public void setInstanceApproved(Integer instanceApproved) {
		this.instanceApproved = instanceApproved;
	}
}