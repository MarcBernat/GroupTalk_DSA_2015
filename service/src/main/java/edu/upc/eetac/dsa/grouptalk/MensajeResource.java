package edu.upc.eetac.dsa.grouptalk;


import edu.upc.eetac.dsa.grouptalk.DAO.MensajeDAO;
import edu.upc.eetac.dsa.grouptalk.DAO.MensajeDAOImpl;
import edu.upc.eetac.dsa.grouptalk.entity.Mensaje;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by marc on 26/10/15.
 *
 */
@Path("mensaje")
public class MensajeResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_MENSAJE)
    public Response createMensaje(@FormParam("idtema") String idtema, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if(idtema==null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        MensajeDAO mensajeDAO = new MensajeDAOImpl();
        Mensaje mensaje = null;
        try {
            mensaje = mensajeDAO.createMensaje(securityContext.getUserPrincipal().getName(), idtema, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + mensaje.getIdmensaje());
        return Response.created(uri).type(GroupTalkMediaType.grouptalk_MENSAJE).entity(mensaje).build();
    }

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.grouptalk_MENSAJE)
    public Mensaje getMensaje(@PathParam("idmensaje") String idmensaje) {
        MensajeDAO mensajeDAO = new MensajeDAOImpl();
        Mensaje mensaje = null;;
            try {
                mensaje = mensajeDAO.getMensajeById(idmensaje);
                if(mensaje == null)
                    throw new NotFoundException("Sting with id = "+idmensaje+" doesn't exist");
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            return mensaje;
        }


    @Path("/{id}")
    @PUT
    @Consumes(GroupTalkMediaType.grouptalk_MENSAJE)
    @Produces(GroupTalkMediaType.grouptalk_MENSAJE)
    public Mensaje updateUSting(@PathParam("id") String id, Mensaje mensaje) {
        if(mensaje == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(mensaje.getIdmensaje()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(mensaje.getUserid()))
            throw new ForbiddenException("operation not allowed");

        MensajeDAO mensajeDAO = new MensajeDAOImpl();
        try {
            mensaje = mensajeDAO.updateMensaje(userid, mensaje.getIdtema(), mensaje.getContent());
            if(mensaje == null)
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return mensaje;
    }

    @Path("/{id}")
    @DELETE
    public void deleteSting(@PathParam("idmensaje") String idmensaje) {
        String userid = securityContext.getUserPrincipal().getName();
        MensajeDAO mensajeDAO = new MensajeDAOImpl();
        try {
            String ownerid = mensajeDAO.getMensajeById(idmensaje).getUserid();
            if(!userid.equals(ownerid) || !securityContext.isUserInRole("admin"))
                throw new ForbiddenException("operation not allowed");
            if(!mensajeDAO.deleteMensaje(idmensaje))
                throw new NotFoundException("Sting with id = "+idmensaje+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}