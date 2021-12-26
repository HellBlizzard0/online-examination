package com.code.servlets;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.code.dal.orm.setup.Image;
import com.code.services.log4j.Log4j;
import com.code.services.setup.ImageService;

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long imageId;

		String imageStringId = request.getParameter("id");
		if (!imageStringId.isEmpty() && !imageStringId.equals("undefined") && imageStringId != "0") {
			imageId = Long.parseLong(imageStringId);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		}

		Image image = null;
		try {
			image = ImageService.getImageById(imageId);
		} catch (Exception e) {
			Log4j.traceErrorException(ImageServlet.class, e, "ImageServlet");
		}
		if (image == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		}

		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(image.getType());
		response.setContentLength(image.getContent().length);
		response.setHeader("Content-Disposition", "inline; filename=\"" + "img_" + image.getId() + "\"");

		BufferedOutputStream output = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
			output.write(image.getContent());
		} finally {
			close(output);
		}
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				Log4j.traceErrorException(ImageServlet.class, e, "ImageServlet");
			}
		}
	}
}
