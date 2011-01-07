package com.melodo.ofxbridge.bank;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.melodo.ofxbridge.core.AbstractOfxProvider;

import java.io.*;
import java.net.URL;
import java.util.Calendar;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 9, 2009
 * Time: 12:05:19 PM
 */
public class Pekao24Connector extends AbstractConnector {


    public<P extends AbstractOfxProvider> String getBillingInfoDocument(P provider) {
        String customer = provider.getCustomer();
        String password = provider.getPassword();
        String account = provider.getAccount();
        WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_6);
        String text = "";
        try {
            HtmlPage page = client.getPage("https://www.pekao24.pl/");
            HtmlForm form = page.getForms().get(0);
            form.getInputByName("parUsername").setValueAttribute(customer);
            HtmlInput submitButton = form.getInputByName("butLogin");
            HtmlPage passwordPage = submitButton.click();

            HtmlForm passForm = passwordPage.getFormByName("frmPassword");
            StringBuffer pass = new StringBuffer();
            for (int i = 0;i<16;i++){
                HtmlInput formInput = passForm.getElementById("f" + i);
                boolean writable  = !formInput.isDisabled();
                if (writable){
                     pass.append(password.charAt(i));
                    formInput.setValueAttribute(String.valueOf(password.charAt(i)));
                }
            }
            HtmlInput passwordSubmitButton = passwordPage.getElementByName("fSubmit");
            HtmlPage accountPage = passwordSubmitButton.click();
            HtmlForm accountForm = accountPage.getFormByName("frmLanguage");
            String sessionID = accountForm.getInputByName("BV_SessionID").getValueAttribute();
            HtmlPage accountDetailsPage = (HtmlPage) accountPage.executeJavaScript("submitForm("+account+")").getNewPage();

            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH,-1);
            String fromMonth = ""+c.get(Calendar.MONTH);
            String fromYear = ""+ c.get(Calendar.YEAR);
            c.add(Calendar.MONTH,1);
            String toMonth = ""+c.get(Calendar.MONTH);
            String toYear = ""+ c.get(Calendar.YEAR);
            String toDay = ""+c.get(Calendar.DAY_OF_MONTH);
            WebRequestSettings requestSettings = new WebRequestSettings(new URL("https://www.pekao24.pl/MCP/CurrentAccount.htm?BV_SessionID=" + sessionID + "&BV_EngineID=1&cmdAction=MovementsSearch&parActionFlag=NewAction&parPagesCounter=1&parCurrentPage=F&parCallerPageAction=MovementsSearch&PassedAccNum=" + account + "&txtFromDayF=01&txtFromMonthF=" + fromMonth + "&txtFromYearF=" + fromYear + "&txtStartAmountAF=&txtStartAmountBF=&txtTransactionId=Wszystkie&txtToDayF=" + toDay + "&txtToMonthF=" + toMonth + "&txtToYearF=" + toYear + "&txtEndAmountAF=&txtEndAmountBF="));
            client.loadWebResponse(requestSettings);
            requestSettings = new WebRequestSettings(new URL("https://www.pekao24.pl:443/MCP/TextCreator_OperHist.htm?BV_SessionID=" + sessionID + "&BV_EngineID=1"));
            requestSettings.setCharset("Windows-1250");
            HtmlPage exportPage = client.getPage(requestSettings);
            TextPage result = exportPage.getAnchors().get(1).click();
            InputStream in = result.getWebResponse().getContentAsStream();
            text = getContentAsString(in);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return text;
    }

}