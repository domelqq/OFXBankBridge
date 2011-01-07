package com.melodo.ofxbridge.bank;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.melodo.ofxbridge.core.AbstractOfxProvider;

import java.io.*;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 9, 2009
 * Time: 12:05:19 PM
 */
public class MbankConnector extends AbstractConnector {
     public<P extends AbstractOfxProvider> String getBillingInfoDocument(P provider) {
        String customer = provider.getCustomer();
        String password = provider.getPassword();
        String account = provider.getAccount();

        try {
            WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_6);
            HtmlPage loginPage = client.getPage("https://www.mbank.com.pl/");
            HtmlForm loginForm = loginPage.getForms().get(0);
            loginForm.getInputByName("customer").setValueAttribute(customer);
            loginForm.getInputByName("password").setValueAttribute(password);
            HtmlButton submitButton = loginForm.getElementById("confirm");
            HtmlPage accountsPage = submitButton.click();
            FrameWindow mainFrame = accountsPage.getFrameByName("MainFrame");
            HtmlPage accountPage = ((HtmlPage) mainFrame.getEnclosedPage()).getAnchorByText(account).click();
            HtmlPage historyPage = accountPage.getAnchorByText("Historia").click();
            HtmlForm historyForm = historyPage.getForms().get(0);
            historyForm.getInputByName("export_oper_history_check").setChecked(true);
            historyPage.getForms().get(0).getSelectByName("export_oper_history_format").setSelectedAttribute("CSV",true);
            historyForm.getInputByName("daterange_from_year").setValueAttribute("2009");
            HtmlButton historyFormSubmit = historyForm.getElementById("Submit");
            TextPage resultPage = historyFormSubmit.click();
            return resultPage.getWebResponse().getContentAsString("windows-1250");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


//       ((Request) httpServletRequest).setHandled(true);

        //       httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        return null;
    }
}
