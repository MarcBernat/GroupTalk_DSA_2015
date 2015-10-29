package edu.upc.eetac.dsa.grouptalk;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by marc on 26/10/15.
 */
public class GroupTalkResourcesConfig extends ResourceConfig {
    public GroupTalkResourcesConfig() {
        packages("edu.upc.eetac.dsa.grouptalk");
        packages("edu.upc.eetac.dsa.grouptalk.auth");
    }
}
