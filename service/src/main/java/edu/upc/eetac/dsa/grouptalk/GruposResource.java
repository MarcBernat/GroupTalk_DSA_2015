package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.DAO.GruposDAO;
import edu.upc.eetac.dsa.grouptalk.DAO.GruposDAOImpl;
import edu.upc.eetac.dsa.grouptalk.DAO.SuscripcionDAO;
import edu.upc.eetac.dsa.grouptalk.DAO.SuscripcionDAOImpl;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;
import edu.upc.eetac.dsa.grouptalk.entity.Grupos;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by marc on 29/10/15.
 */
@Path("grupos")
public class GruposResource {
    @Context
    private SecurityContext securityContext;

    @Path("/{id}")
    @GET
    @Produces(GroupTalkMediaType.grouptalk_GRUPOS)
    public Grupos getGrupos(@PathParam("idgrupo") String idgrupo) throws URISyntaxException {
        GruposDAO gruposDAO = new GruposDAOImpl();
        Grupos grupos = null;
        try {
            grupos = gruposDAO.getGrupo(idgrupo);
            if(grupos == null)
                throw new NotFoundException("Sting with id = "+idgrupo+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return grupos;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_GRUPOS)
    public Response createGrupo(@FormParam("fullname") String nombretema, @Context UriInfo uriInfo) throws URISyntaxException {
        if(nombretema==null)
            throw new BadRequestException("all parameters are mandatory");
        GruposDAO gruposDAO = new GruposDAOImpl();
        Grupos grupos = null;

        if(securityContext.isUserInRole("admin"))
            throw new ForbiddenException("operation not allowed");
        try {
            grupos = gruposDAO.createGrupo(nombretema);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + grupos.getIdgrupo());
        return Response.created(uri).type(GroupTalkMediaType.grouptalk_GRUPOS).entity(grupos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void entrarGrupo(@FormParam("idgrupo") String idgrupo, @FormParam("idusuario") String idusuario) throws URISyntaxException
    {
        if(idgrupo==null || idusuario==null)
            throw new BadRequestException("all parameters are mandatory");

        SuscripcionDAO suscripcionDAO = new SuscripcionDAOImpl();

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            suscripcionDAO.createSuscripcion(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void borrarGrupo(@FormParam("idgrupo") String idgrupo, @FormParam("idusuario") String idusuario) throws URISyntaxException
    {
        if(idgrupo==null || idusuario==null)
            throw new BadRequestException("all parameters are mandatory");

        SuscripcionDAO suscripcionDAO = new SuscripcionDAOImpl();

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        boolean dentro;
        try {
             dentro = suscripcionDAO.getSuscripcionByUsuario(idusuario,idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        if(dentro) {
            try {
                suscripcionDAO.deleteSuscripcion(idusuario, idgrupo);
            } catch (SQLException e) {
                throw new InternalServerErrorException();
            }
        }
    }
}
