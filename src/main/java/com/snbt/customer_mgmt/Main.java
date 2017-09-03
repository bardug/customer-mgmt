package com.snbt.customer_mgmt;

import com.google.gson.Gson;
import com.snbt.customer_mgmt.bl.DBService;
import com.snbt.customer_mgmt.bl.InMemoryDBService;
import com.snbt.customer_mgmt.domain.Customer;
import com.snbt.customer_mgmt.ws.CustomerController;
import com.snbt.customer_mgmt.ws.Response;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
        // newEmbeddedJetty();
        new Server();
        DBService customerDBService = new InMemoryDBService();
        CustomerController.go(customerDBService);
/*
        get("/hello", (request, response) -> "world");
        get("/customers", (request, response) -> "world");

        post("/customers", (request, response) -> {
            response.type("application/json");
            Customer customer = new Gson().fromJson(request.body(), Customer.class);
            customerDBService.create(customer);
            return new Gson()
                    .toJson(new Response(Response.Status.SUCCESS));
        });
*/
    }

    private static Server newEmbeddedJetty() {
        Server server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(8443);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setHost("127.0.0.1");
        http.setPort(8080);
        http.setIdleTimeout(30000);

        server.addConnector(http);
/*
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.setSessionHandler(new SessionHandler());

        // Setup Spring context
        context.addEventListener(new ContextLoaderListener());
        context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "classpath:spring-all.xml");
*/


        return server;
    }
}
