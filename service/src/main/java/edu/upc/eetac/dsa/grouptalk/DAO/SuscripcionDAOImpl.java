package edu.upc.eetac.dsa.grouptalk.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by marc on 28/10/15.
 */
public class SuscripcionDAOImpl implements SuscripcionDAO{
    public boolean createSuscripcion(String userid, String grupoid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(SuscripcionDAOQuery.CREATE_SUSCRIPCION);
            stmt.setString(1, userid);
            stmt.setString(2, grupoid);
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
        return getSuscripcionByUsuario(userid, grupoid);
    }

    @Override
    public boolean getSuscripcionByUsuario(String userid, String grupoid) throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(SuscripcionDAOQuery.CREATE_SUSCRIPCION);
            stmt.setString(1, userid);
            stmt.setString(2, grupoid);
            stmt.executeUpdate();

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }


    @Override
    public boolean deleteSuscripcion(String userid, String grupoid) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(SuscripcionDAOQuery.DELETE_SUSCRIPCION);
            stmt.setString(1, userid);
            stmt.setString(2, grupoid);

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
