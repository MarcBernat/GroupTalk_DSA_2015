package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.entity.Mensaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by marc on 5/10/15.
 */
public class MensajeDAOImpl implements MensajeDAO {
    @Override
    public Mensaje createMensaje(String userid, String idtema, String content) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        String idmensaje = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(MensajeDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                idmensaje = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(MensajeDAOQuery.CREATE_Mensaje);
            stmt.setString(1, idmensaje);
            stmt.setString(2, idtema);
            stmt.setString(3, userid);
            stmt.setString(4, content);
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
        return getMensajeById(idmensaje);
    }

    @Override
    public Mensaje getMensajeById(String id) throws SQLException {
        Mensaje Mensaje = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(MensajeDAOQuery.GET_Mensaje_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mensaje = new Mensaje();
                Mensaje.setIdmensaje(rs.getString("idmensaje"));
                Mensaje.setIdtema(rs.getString("idtema"));
                Mensaje.setUserid(rs.getString("userid"));
                Mensaje.setContent(rs.getString("content"));
                Mensaje.setLastModified(rs.getTimestamp("last_modified").getTime());
                Mensaje.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return Mensaje;
    }

    @Override
    public Mensaje updateMensaje(String id, String subject, String content) throws SQLException {
        Mensaje Mensaje = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(MensajeDAOQuery.UPDATE_Mensaje);
            stmt.setString(1, subject);
            stmt.setString(2, content);
            stmt.setString(3, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                Mensaje = getMensajeById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return Mensaje;
    }

    @Override
    public boolean deleteMensaje(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(MensajeDAOQuery.DELETE_Mensaje);
            stmt.setString(1, id);

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
