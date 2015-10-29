package edu.upc.eetac.dsa.grouptalk.entity;

/**
 * Created by marc on 28/10/15.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by marc on 5/10/15.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Suscripcion {
    @InjectLinks({})
    private List<Link> links;
    private String userid;
    private String grupoid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGrupoid() {
        return grupoid;
    }

    public void setGrupoid(String grupoid) {
        this.grupoid = grupoid;
    }
}
