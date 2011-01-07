package com.melodo.ofxbridge.bank;

import com.melodo.ofxbridge.core.AbstractOfxProvider;
import com.melodo.ofxbridge.core.BillingInfo;
import com.melodo.ofxbridge.core.BillingPosition;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 9, 2009
 * Time: 12:04:22 PM
 */
public abstract class AbstractParser {
    public abstract <P extends AbstractOfxProvider> BillingInfo getBillingInfo(Reader reader, P provider);

    protected String getId(String string) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(string.getBytes(), 0, string.length());
            return new BigInteger(1, m.digest()).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return string;
    }

    protected void setId(BillingPosition position){
        position.setId(position.getDatePosted()+getId(position.getDescription())+position.getTransactionAmount());
    }



    
}
