package com.test;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

@Path("/hello")
public class GreetingResource {

    private final MyHostService service;

    String serviceName = "MY_HOST";
    String serviceHost;
    String servicePort;
    String url;
    
    public GreetingResource(){

        if(System.getenv("MY_HOST_SERVICE_NAME") != null){
            serviceName = System.getenv("MY_HOST_SERVICE_NAME");
        }
        serviceHost = System.getenv(serviceName + "_SERVICE_HOST");
        servicePort = System.getenv(serviceName + "_SERVICE_PORT");
        url = "http://" + serviceHost + ":" + servicePort + "/";

        System.out.println("Service name parameter: " + serviceName);
        System.out.println("Service host parameter: " + serviceName + "_SERVICE_HOST");
        System.out.println("Service port parameter: " + serviceName + "_SERVICE_PORT");
        System.out.println("URL = " + url);

        service = RestClientBuilder.newBuilder()
        .baseUri(URI.create(url))
        .build(MyHostService.class);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String result;
        
        try{
            result = service.getHostName();
        }
        catch(Exception ex){
            result = "Something went wrong when calling the service. The service name is currently set to " 
            + serviceName + " This can be set by the environment variable MY_HOST_SERVICE_NAME. \nException: " 
            + ex.getMessage();
        }

        return result;
    }
}