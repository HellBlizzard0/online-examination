package com.code.services.attach;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.code.dal.orm.attach.Attachment;
import com.code.enums.FlagsEnum;
import com.code.services.log4j.Log4j;

@Path("/AttachmentServiceCallBack")
public class AttachmentServiceCallBack {
	public static final String SERVICE_URL = "/rest/AttachmentServiceCallBack/updateAttachmentId";
	
	@POST
	@Path("/updateAttachmentId")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updateAttachmentId(String data) {
		JSONObject result = null;
		try {
			result = new JSONObject(data);
			String token = result.getString("TOKEN");
			String contentId = result.getString("CONTENT_ID");
			String fileName = result.getString("FILE_NAME");
			
			if(!contentId.equals(FlagsEnum.ALL.getCode())) {
				String [] tokenSplitted = token.split("_");
				
				Attachment attachment = new Attachment();
				attachment.setEntityId(Long.parseLong(tokenSplitted[1]));
				attachment.setEntityName(tokenSplitted[0]);
				attachment.setAttachmentId(contentId);
				attachment.setFileName(fileName);
				AttachmentService.addAttachment(attachment);
			}
			result = new JSONObject();
			result.put("RESULT", "SUCCESS");
		} catch (Exception e) {
			Log4j.traceErrorException(AttachmentServiceCallBack.class, e, "AttachmentServiceCallBack");
		}
		return Response.status(Status.OK).entity(result.toString()).build();
	}
}