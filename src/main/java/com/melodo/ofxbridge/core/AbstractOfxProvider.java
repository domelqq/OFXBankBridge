package com.melodo.ofxbridge.core;

import com.melodo.ofxbridge.bank.AbstractConnector;
import com.melodo.ofxbridge.bank.AbstractParser;
import com.melodo.ofxbridge.bank.BankFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: domel
 * Date: Apr 14, 2010
 * Time: 10:37:14 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOfxProvider {
    private String account;
    private String customer;
    private String password;
    private String name;
    private String type;

    protected boolean saveFile = false;


    public String getBankRawData(){
        AbstractConnector connector = BankFactory.getConnector(type);
        String infoDocument = connector.getBillingInfoDocument(this);
        infoDocument = cleanUp(infoDocument);
        if (saveFile) {
            try {
                FileWriter writer = new FileWriter("data/"+account.replaceAll(" ","") + ".txt");
                writer.write(infoDocument);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return infoDocument;
    }

    public String getOfxData() {
        String infoDocument = getBankRawData();
        AbstractParser parser = BankFactory.getParser(type);
        StringReader reader = new StringReader(infoDocument);
        BillingInfo billingInfo = parser.getBillingInfo(reader, this);
       // checkTransactionIds(billingInfo);
        if (billingInfo.getAccountType() == null){
            billingInfo.setAccountType(name);
        }
        billingInfo.setBankId(type);
        billingInfo.setAccountId(account);
        OFXBuilder builder = new OFXBuilder();
        try {
            return builder.build(billingInfo);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }



    private String cleanUp(String document) {
        document = document.replace((char) 322, 'l').replace((char) 321, 'L');
        document = document.replace((char) 261, 'a').replace((char) 260, 'A');
        document = document.replace((char) 281, 'e').replace((char) 280, 'E');
        document = document.replace((char) 380, 'z').replace((char) 379, 'Z');
        document = document.replace((char) 378, 'z').replace((char) 377, 'Z');
        document = document.replace((char) 263, 'c').replace((char) 262, 'C');
        document = document.replace((char) 347, 's').replace((char) 346, 'S');
        document = document.replace((char) 243, 'o').replace((char) 242, 'O');
        document = document.replace((char) 324, 'n').replace((char) 323, 'N');
        return document;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
