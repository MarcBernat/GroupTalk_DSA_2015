package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Suscripcion;

import java.sql.SQLException;

/**
 * Created by marc on 28/10/15.
 */
public interface SuscripcionDAO {
    public boolean getSuscripcionByUsuario(String userid, String grupoid) throws SQLException;
    public boolean createSuscripcion(String userid, String grupoid) throws SQLException;
    public boolean deleteSuscripcion(String userid, String grupoid) throws SQLException;
}
