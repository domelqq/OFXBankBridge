package com.melodo.ofxbridge.inbound;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 10, 2009
 * Time: 12:58:48 PM
 */
public class OfxServer extends AbstractHandler {

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        HttpRequestOfxProvider ofxProvider = new HttpRequestOfxProvider(request);
        String ofx = ofxProvider.getOfxData();
        System.out.println(ofx);
        response.getOutputStream().write(ofx.getBytes());
        if (request instanceof Request) {
            ((Request) request).setHandled(true);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static void main(String[] args) {
        Server server = new Server(8082);
        server.setHandler(new OfxServer());
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
