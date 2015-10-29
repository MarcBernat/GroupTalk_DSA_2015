package edu.upc.eetac.dsa.grouptalk.DAO;

import edu.upc.eetac.dsa.grouptalk.auth.UserInfo;
import edu.upc.eetac.dsa.grouptalk.entity.AuthToken;

import java.sql.SQLException;

/**
 * Created by marc on 5/10/15.
 */
public interface AuthTokenDAO {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws  SQLException;

}
