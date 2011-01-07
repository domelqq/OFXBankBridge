package com.melodo.ofxbridge.inbound;

import com.melodo.ofxbridge.core.AbstractOfxProvider;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: domel
 * Date: Apr 14, 2010
 * Time: 11:50:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequestOfxProvider extends AbstractOfxProvider{
    public HttpRequestOfxProvider(HttpServletRequest request) {
        super();
        setAccount(changeSigns(request.getParameter("account")));
        setType(changeSigns(request.getParameter("type")));
        setName(changeSigns(request.getParameter("name")));
        setCustomer(changeSigns(request.getParameter("customer")));
        setPassword(changeSigns(request.getParameter("password")));
    }

    public String changeSigns(String parameter){
        // we need to do that because of money well problem
        // replace
        parameter = parameter.replaceAll("__"," ");
        parameter = parameter.replaceAll("_hash","#");
        parameter = parameter.replaceAll("_plus","+");
        return parameter;
    }
}
