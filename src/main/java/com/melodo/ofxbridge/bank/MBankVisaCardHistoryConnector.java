package com.melodo.ofxbridge.bank;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.melodo.ofxbridge.core.AbstractOfxProvider;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: domel
 * Date: May 6, 2010
 * Time: 4:52:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MBankVisaCardHistoryConnector extends AbstractConnector {
    public <P extends AbstractOfxProvider> String getBillingInfoDocument(P provider) {
        String customer = provider.getCustomer();
        String password = provider.getPassword();
        String account = provider.getAccount();
        try {
            WebClient client = new WebClient(BrowserVersion.FIREFOX_3);
            HtmlPage loginPage = client.getPage("https://www.mbank.com.pl/");
            HtmlForm loginForm = loginPage.getForms().get(0);
            loginForm.getInputByName("customer").setValueAttribute(customer);
            loginForm.getInputByName("password").setValueAttribute(password);
            HtmlButton submitButton = loginForm.getElementById("confirm");
            HtmlPage accountsPage = submitButton.click();
            FrameWindow mainFrame = accountsPage.getFrameByName("MainFrame");
            HtmlPage cardsPage = ((HtmlPage) mainFrame.getEnclosedPage()).getAnchorByText("Karty").click();
            HtmlPage cardPage = cardsPage.getAnchorByText(account).click();
            List<HtmlAnchor> links = cardPage.getAnchors();
            HtmlPage historicOperationsPage = null;
            HtmlPage historyPage = null;
            StringBuffer text = new StringBuffer();
            for (HtmlAnchor link : links) {
                if (link.getTextContent().startsWith("Wyciągi historyczne")){
                    historicOperationsPage = link.click();
                    break;
                }
            }
            HtmlDivision historicalStatementsList = (HtmlDivision) historicOperationsPage.getElementById("historicalStatementsList");
            HtmlUnorderedList ul = (HtmlUnorderedList) historicalStatementsList.getElementsByTagName("ul").get(0);
            Iterable<HtmlElement> childElements = ul.getChildElements();
            for (HtmlElement childElement : childElements) {
                DomNodeList<HtmlElement> historicLinks = childElement.getElementsByTagName("a");
                historicLinks.size();
                if (historicLinks.size() > 0){
                    HtmlAnchor historicLink = (HtmlAnchor) historicLinks.get(0);
                    historyPage = historicLink.click();
                    HtmlDivision listOfOperations = (HtmlDivision) historyPage.getElementById("cc_current_operations");
                                HtmlUnorderedList unorderedList = (HtmlUnorderedList) listOfOperations.getElementsByTagName("ul").get(0);
                                List<HtmlElement> listItems = unorderedList.getElementsByTagName("li");

                                if (listItems.size() > 1){
                                    for (int i = 1, listItemsSize = listItems.size()-1; i < listItemsSize; i++) {
                                        HtmlListItem listItem = (HtmlListItem) listItems.get(i);
                                        String date = listItem.getHtmlElementsByTagName("p").get(1).getElementsByTagName("span").get(1).getTextContent();
                                        String type = listItem.getHtmlElementsByTagName("p").get(2).getElementsByTagName("a").get(0).getTextContent();
                                        String merchant = listItem.getHtmlElementsByTagName("p").get(3).getElementsByTagName("span").get(0).getTextContent();
                                        String amoutOrg = listItem.getHtmlElementsByTagName("p").get(4).getElementsByTagName("span").get(0).getTextContent();
                                        String amountPLN = listItem.getHtmlElementsByTagName("p").get(5).getElementsByTagName("span").get(0).getTextContent();
                                        text.append(date).append(";").append(type).append(";").append(merchant).append(";").append(amoutOrg).append(";").append(amountPLN).append("\n");

                                    }

                                }

                }
            }



            return text.toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
