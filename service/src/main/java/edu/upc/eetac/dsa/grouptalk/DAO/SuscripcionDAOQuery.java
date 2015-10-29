package edu.upc.eetac.dsa.grouptalk.DAO;

/**
 * Created by marc on 28/10/15.
 */
public interface SuscripcionDAOQuery {
    public final static String CREATE_SUSCRIPCION = "insert into suscripcion (userid, grupoid) values (UNHEX(?), UNHEX(?))";
    public final static String GET_SUSCRIPCION= "select hex(userid) as userid, hex(grupoid) as grupoid from suscripcion where idgrupo=unhex(?) and idgrupoidgrupo=unhex(?)";
    public final static String DELETE_SUSCRIPCION = "delete from grupos where idgrupo=unhex(?)";
}
