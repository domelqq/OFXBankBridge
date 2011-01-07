package com.melodo.ofxbridge.bank;

import com.melodo.ofxbridge.core.AbstractOfxProvider;
import com.melodo.ofxbridge.core.BillingInfo;
import com.melodo.ofxbridge.core.BillingPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 8, 2009
 * Time: 2:03:43 PM
 */
public class MbankVisaCardParser extends AbstractParser {
    public<P extends AbstractOfxProvider> BillingInfo getBillingInfo(Reader reader, P provider) {
        BillingInfo info = new BillingInfo();
        info.setAccountType("CREDITLINE");

        info.setCurrency("PLN");
        BufferedReader br = new BufferedReader(reader);
        try {

            createLines(br, info);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return info;
    }

    private void createLines(BufferedReader br, BillingInfo info) throws IOException {
        List<BillingPosition> positions = info.getPositions();
        while (br.ready()) {
            String line = br.readLine();
            if (line != null && line.length() > 0) {
                //
                BillingPosition billingPosition = new BillingPosition();
                String[] strings = getSplit(line);
                billingPosition.setDatePosted(getDate(strings[0]));
                billingPosition.setTransactionType(getTransactionType(strings[1]));
                billingPosition.setDescription(strings[1]);
                String name = strings[2] + " " + strings[1];
                billingPosition.setName(name.trim());
                billingPosition.setTransactionAmount(strings[4].replace(" ", "").replace(",", "."));
                setId(billingPosition);
                positions.add(billingPosition);
            } else {
                break;
            }
        }
    }

    private String getDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


        try {
            Date date = format.parse(string);
            format.applyPattern("yyyyMMdd");
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return string;
    }





    private String[] getSplit(String line) {
        return line.split(";");
    }

    String getTransactionType(String mbankType) {
        if (mbankType.startsWith("SPLATA")) {
            return "INT";
        } else if (mbankType.startsWith("ZWROT")) {
            return "INT";
        } else if (mbankType.startsWith("PODATEK OD ODSETEK KAPITALOWYCH")) {
            return "DEBIT";
        } else if (mbankType.startsWith("KREDYT")) {
            return "DEBIT";
        } else if (mbankType.startsWith("PRZELEW")) {
            return "XFER";
        } else if (mbankType.startsWith("ZAKUP")) {
            return "XFER";
        } else {
            return "DEBIT";
        }
    }



}