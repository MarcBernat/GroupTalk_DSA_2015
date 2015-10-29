package edu.upc.eetac.dsa.grouptalk;

/**
 * Created by marc on 29/10/15.
 */

import edu.upc.eetac.dsa.grouptalk.DAO.*;
import edu.upc.eetac.dsa.grouptalk.entity.Temas;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Path("temas")
public class TemasResource {
    @Context
    private SecurityContext securityContext;

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.grouptalk_TEMAS)
    public Temas getTema(@PathParam("idtema") String idtema, @PathParam("idusuario") String idusuario) throws URISyntaxException {
        TemasDAO temasDAO = new TemasDAOImpl();
        SuscripcionDAO suscripcionDAO = new SuscripcionDAOImpl();
        Temas temas = null;

        if(idtema==null)
            throw new BadRequestException("all parameters are mandatory");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            temas = temasDAO.getTemaById(idtema);
            if(temas == null)
                throw new NotFoundException("Sting with id = "+temas+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        boolean dentro;
        try {
            dentro = suscripcionDAO.getSuscripcionByUsuario(idusuario,temas.getIdgrupo());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        if(dentro)
            return temas;
        else throw new ForbiddenException("operation not allowed");
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_GRUPOS)
    public Response createTema(@FormParam("idusuario") String idusuario, @FormParam("idgrupo") String idgrupo, @FormParam("idgrupo") String subject, @Context UriInfo uriInfo) throws URISyntaxException {
        if (idusuario == null || idgrupo == null || subject == null)
            throw new BadRequestException("all parameters are mandatory");
        TemasDAO temasDAO = new TemasDAOImpl();
        Temas temas = null;
        SuscripcionDAO suscripcionDAO = new SuscripcionDAOImpl();
        URI uri = null;

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        boolean dentro;
        try {
            dentro = suscripcionDAO.getSuscripcionByUsuario(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        if (dentro) {
            try {
                temas = temasDAO.createTema(idgrupo, idusuario, subject);
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
            uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + temas.getIdtema());

        }
        return Response.created(uri).type(GroupTalkMediaType.grouptalk_TEMAS).entity(temas).build();
    }

    @Path("/{id}")
    @DELETE
    public void deleteSting(@PathParam("idtema") String idtema) throws URISyntaxException{
        String userid = securityContext.getUserPrincipal().getName();
        TemasDAO temasDAO = new TemasDAOImpl();
        try {
            String ownerid = temasDAO.getTemaById(idtema).getIdusuario();
            if(!userid.equals(ownerid) || !securityContext.isUserInRole("admin"))
                throw new ForbiddenException("operation not allowed");
            if(!temasDAO.deleteTema(idtema))
                throw new NotFoundException("Sting with id = "+idtema+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
