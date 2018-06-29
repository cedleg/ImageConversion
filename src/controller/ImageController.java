package controller;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import dao.ImageDAO;
import model.Image;

@Path("/image")
public class ImageController {

	@Inject
	ImageDAO daoImage;
	
	@GET
	@Path("/{nom}")
	@Produces(value = MediaType.APPLICATION_OCTET_STREAM)
	public Response findImage(@PathParam("nom") String nom, @QueryParam("format") String format) {
		Image img = null;
		try {
			img = daoImage.findByName(nom, format);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Response.status(200)
						.entity(img.getData())
						.header("content-disposition", "inline;filename=image."+img.getMeta())
						.build();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String postImage(@MultipartForm FileUploadForm form) {
		return daoImage.save(form.getFileData(), form.getMeta());
	}
	
}
