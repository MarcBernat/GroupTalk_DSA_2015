package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Temas;

import java.sql.SQLException;

/**
 * Created by marc on 5/10/15.
 */
public interface TemasDAO {
    public Temas createTema(String idgrupo, String idusuario, String subject) throws SQLException;
    public Temas getTemaById(String idtema) throws SQLException;
    public boolean deleteTema(String idtema) throws SQLException;
}
