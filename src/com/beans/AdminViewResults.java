package com.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.entities.ExamResult;
import com.entities.ResultDetailed;
import com.models.ResultModel;

@SessionScoped
@ManagedBean(name = "adminViewResultsBean")
public class AdminViewResults {
	List<ResultDetailed> resultsDetailed;
	public String goTo() {
		resultsDetailed = ResultModel.getAllDetailed();
		return "AdminViewResults";
	}
	public List<ResultDetailed> getResultsDetailed() {
		return resultsDetailed;
	}
	public void setResultsDetailed(List<ResultDetailed> resultsDetailed) {
		this.resultsDetailed = resultsDetailed;
	}
}
