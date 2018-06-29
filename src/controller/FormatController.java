package controller;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.FormatDAO;
import model.Format;

@Path("/format")
public class FormatController {

	@Inject
	FormatDAO daoFormat;
	
	@GET
	@Path("/{nom}")
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response findFormat(@PathParam("nom") String nom) {
		
		Format f = daoFormat.find(nom);
		if(f==null) {
			return Response.status(400).build();
		}
		return Response.status(200).entity(f).build();
	}
	
	@POST
	@Path("/")
	@Consumes(value = {MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response saveFormat(Format toSave) {
		
		if(daoFormat.find(toSave.getName())!=null) {
			return Response.status(409).build();
		}
		Format result = daoFormat.save(toSave);
		return Response.ok().status(201).entity(result).build();
		
	}
	
	@PUT
	@Path("/")
	@Consumes(value = {MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response changeFormat(Format toUpdate) {
		
		if(daoFormat.find(toUpdate.getName())==null) {
			return Response.status(404).build();
		}
		Format entity = daoFormat.update(toUpdate);
		return Response.status(202).entity(entity).build();
	}
	
	@DELETE
	@Path("/{nom}")
	@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
	public Response deleteFormat(@PathParam("nom") String nom) {
		Format f = daoFormat.find(nom);
		if(f==null) {
			return Response.status(400).build();
		}
		Format removed = daoFormat.remove(f);
		return Response.accepted().entity(removed).build();
	}
	
	
}
