package com.melodo.ofxbridge.bank;

import com.melodo.ofxbridge.core.AbstractOfxProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 10, 2009
 * Time: 12:59:21 PM
 */
public abstract class AbstractConnector {
    public abstract <P extends AbstractOfxProvider> String getBillingInfoDocument(P ofxProvider);

    protected String getContentAsString(InputStream in) {
        try {
            InputStreamReader streamReader = new InputStreamReader(in, "windows-1250");
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while (line != null) {
                line = bufferedReader.readLine();
                if (line != null) {
                    buffer.append(line).append("\n");
                }
            }
            streamReader.close();


            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
