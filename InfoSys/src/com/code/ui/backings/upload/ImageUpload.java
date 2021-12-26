package com.code.ui.backings.upload;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.code.dal.orm.setup.Image;
import com.code.services.log4j.Log4j;
import com.code.services.setup.ImageService;
import com.code.ui.backings.base.BaseBacking;

@ManagedBean(name = "imageUpload")
@ViewScoped
public class ImageUpload extends BaseBacking implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long imageId = 0L;
	byte[] content;
	String contentType;
	UploadedFile file;

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public Long getImageId() {
		return imageId;
	}

	public void init() {
		super.init();
	}

	public Long returnId() {
		return imageId;
	}

	public void uploadListener(FileUploadEvent event) throws Exception {
		uploadImage(event);
	}

	/**
	 * Upload image virtually
	 * 
	 * @param event
	 * @throws IOException
	 */
	private void uploadImage(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		content = file.getContents();
		contentType = file.getContentType();
		if (content == null || content.length == 0) { // No File Upload.
			return;
		}
	}

	/**
	 * Insert Image in Blob in DB
	 */
	public void realUpload() {
		try {
			if (imageId == 0) {
				Image image = new Image();
				if (content != null) {
					image.setContent(content);
					image.setType(contentType);
					ImageService.insertImage(image);
					imageId = image.getId();
				}
			} else {
				Image image = ImageService.getImageById(imageId);
				if (content != null) {
					image.setContent(content);
					image.setType(contentType);
					ImageService.updateImage(image);
				}
			}
		} catch (Exception e) {
			Log4j.traceErrorException(ImageUpload.class, e, "ImageUpload");
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}