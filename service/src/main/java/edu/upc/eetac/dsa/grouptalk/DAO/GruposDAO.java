package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Grupos;

import java.sql.SQLException;

/**
 * Created by marc on 28/10/15.
 */
public interface GruposDAO {
    public Grupos getGrupo(String idgrupo) throws SQLException;
    public Grupos createGrupo(String fullname) throws SQLException;
    public boolean deleteGrupo(String idgrupo) throws  SQLException;

}
