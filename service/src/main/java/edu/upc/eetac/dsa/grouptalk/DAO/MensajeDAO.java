package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Mensaje;

import java.sql.SQLException;

/**
 * Created by marc on 5/10/15.
 */
public interface MensajeDAO {
    public Mensaje createMensaje(String userid, String idtema, String content) throws SQLException;
    public Mensaje getMensajeById(String idmensaje) throws SQLException;
    public Mensaje updateMensaje(String userid, String idtema, String content) throws SQLException;
    public boolean deleteMensaje(String idmensaje) throws SQLException;
}
