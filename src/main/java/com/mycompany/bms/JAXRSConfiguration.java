package com.mycompany.bms;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
//Changed from resources to api
@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {
    
}
