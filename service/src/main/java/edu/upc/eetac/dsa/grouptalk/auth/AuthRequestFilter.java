package edu.upc.eetac.dsa.grouptalk.auth;

import edu.upc.eetac.dsa.grouptalk.DAO.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.grouptalk.entity.Role;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by marc on 29/10/15.
 */
    @Provider
    @Priority(Priorities.AUTHENTICATION)
    public class AuthRequestFilter implements ContainerRequestFilter {
    /*
    1. Comprueba si es correcto el token
     */
    @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
        /**
         * V1:Añadido al final, permite saltar la auth sin token si accedemos a una URI sin necessidad de autentificació
         * V2: despues de crear el Singleton Authorized, este nos dira sn necessita una authorización o no.
         *         - Sin autorización: continua con el metodo filtro para sacar el rol del usuario.
         *         - Con autorización: hace un return y continuamos con el recurso.
         */

        /* Versión 1
        if (requestContext.getUriInfo().getPath().equals("myresource") && requestContext.getMethod().equals("GET"))
         */
        if(Authorized.getInstance().isAuthorized(requestContext))
            return;

        final boolean secure = requestContext.getUriInfo().getAbsolutePath().getScheme().equals("https");
        String token = requestContext.getHeaderString("X-Auth-Token");
            if (token == null)
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);

        /**
         En el código primero se obtiene la información relativa a la seguridad del usuario
         a través de la clase concreta DAO AuthTokenDAOImpl que implementamos anteriormente. La variable booleana
         secure es cierta si la petición se realiza vía HTTPS y falsa en caso contrario. Esta variable se define
         porque la implementación a medida de un SecurityContext obliga a devolver un booleano que indique si la
         petición es segura o no. Esto es útil cuando en el código de nuestro servicio queramos impedir que se
         procesen peticiones que no hayan sido hechas vía HTTPS. Cualquier excepción que se produzca en el acceso
         a la base de datos se relanza en forma de InternalServerErrorException que es una excepción que proporciona
         Jersey para devolver errores HTTP 500
         */
        try {
            final UserInfo principal = (new AuthTokenDAOImpl()).getUserByAuthToken(token);
            /**
            /  Con getuser del Token sacamos el ID  y su rol a partir del token y lo guardamos en UserInfo
             */
            if(principal==null)
                throw new WebApplicationException("auth token doesn't exists", Response.Status.UNAUTHORIZED);

            /**
             * Resumiendo, con el filtro que acabamos de implementar conseguimos:

             Rechazar peticiones que no vengan con información de autenticación con error 401 - Unauthorized.
             Hacer accesible al servicio a través de un SecurityContext:
             el identificador de usuario autenticado que realiza la petición.
             el/los role/s que tiene asociado/s.
             la seguridad del canal utilizado para transportar la petición.
             el tipo de esquema de autenticación utilizado.
             */
            requestContext.setSecurityContext(new SecurityContext()
            {
                @Override
                public Principal getUserPrincipal() {
                    return principal;
                }

                @Override
                public boolean isUserInRole(String s) {
                    List<Role> roles = null;
                    if (principal != null) roles = principal.getRoles();
                    return (roles.size() > 0 && roles.contains(Role.valueOf(s)));
                }

                @Override
                public boolean isSecure() {
                    return secure;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "X-Auth-Token";
                }
            });
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        }
    }

