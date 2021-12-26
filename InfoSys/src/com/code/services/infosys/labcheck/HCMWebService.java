package com.code.services.infosys.labcheck;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public class HCMWebService {
	@WebMethod(operationName = "getLabCheckResults")
	@WebResult(name = "results")
	public String HCMSoapWebService(@WebParam(name = "socialIds") @XmlElement(required = true) String message) {
		return LabCheckService.labCheckSoapHCMAnalysis(message);
	}
}
