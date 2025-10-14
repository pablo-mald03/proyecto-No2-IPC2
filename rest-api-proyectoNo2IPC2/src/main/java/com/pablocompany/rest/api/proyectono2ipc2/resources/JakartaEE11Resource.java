package com.pablocompany.rest.api.proyectono2ipc2.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author 
 */
@Path("inicio")
public class JakartaEE11Resource {
    
    @GET
    public Response ping(){
        
        return Response
                .ok("ping Jakarta EE")
                .build();
    }
}
