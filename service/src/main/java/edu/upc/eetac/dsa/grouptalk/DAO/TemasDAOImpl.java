package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Temas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marc on 5/10/15.
 */
public class TemasDAOImpl implements TemasDAO {
    @Override
    public Temas createTema(String idgrupo, String idusuario, String subject) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String idtema = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TemasDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                idtema = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(TemasDAOQuery.CREATE_TEMA);
            stmt.setString(1, idtema);
            stmt.setString(2, idgrupo);
            stmt.setString(3, idusuario);
            stmt.setString(4, subject);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getTemaById(idtema);
    }

    @Override
    public Temas getTemaById(String idtema) throws SQLException {
        Temas tema= null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TemasDAOQuery.GET_TEMA_BY_ID);
            stmt.setString(1, idtema);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tema = new Temas();
                tema.setIdtema(rs.getString("idtema"));
                tema.setIdgrupo(rs.getString("idgrupo"));
                tema.setSubject(rs.getString("subject"));
                tema.setLastModified(rs.getTimestamp("last_modified").getTime());
                tema.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return tema;
    }


    @Override
    public boolean deleteTema(String idtema) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(TemasDAOQuery.DELETE_TEMA);
            stmt.setString(1, idtema);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}
