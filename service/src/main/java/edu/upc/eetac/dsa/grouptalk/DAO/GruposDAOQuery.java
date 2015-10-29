package edu.upc.eetac.dsa.grouptalk.DAO;

/**
 * Created by marc on 28/10/15.
 */
public class GruposDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREATE_GRUPO = "insert into grupos (idgrupo, fullname) values (UNHEX(?), ?)";
    public final static String GET_GRUPO_BY_ID= "select hex(idgrupo) as idgrupo, fullname, creation_timestamp from grupos where idgrupo=unhex(?)";
    public final static String DELETE_GRUPO = "delete from grupos where idgrupo=unhex(?)";
}
