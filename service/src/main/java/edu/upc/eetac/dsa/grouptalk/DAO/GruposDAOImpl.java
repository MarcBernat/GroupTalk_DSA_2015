package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Grupos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marc on 28/10/15.
 */
public class GruposDAOImpl implements GruposDAO {
    @Override
    public Grupos createGrupo(String fullname) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        String idgrupo = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GruposDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                idgrupo = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(GruposDAOQuery.CREATE_GRUPO);
            stmt.setString(1, idgrupo);
            stmt.setString(2, fullname);
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
        return getGrupo(idgrupo);
    }

    @Override
    public Grupos getGrupo(String idgrupo) throws SQLException{
        Grupos grupo= null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GruposDAOQuery.GET_GRUPO_BY_ID);
            stmt.setString(1, idgrupo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                grupo = new Grupos();
                grupo.setIdgrupo(rs.getString("idgrupo"));
                grupo.setFullname(rs.getString("fullname"));
                grupo.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return grupo;
    }


    @Override
    public boolean deleteGrupo(String idgrupo) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(GruposDAOQuery.DELETE_GRUPO);
            stmt.setString(1, idgrupo);

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
