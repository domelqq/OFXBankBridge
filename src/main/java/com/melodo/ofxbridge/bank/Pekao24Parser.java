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
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: domel
 * Date: Apr 9, 2010
 * Time: 9:19:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pekao24Parser extends AbstractParser {
    public <P extends AbstractOfxProvider> BillingInfo getBillingInfo(Reader reader, P provider) {
        BillingInfo info = new BillingInfo();

        info.setCurrency("PLN");
        BufferedReader br = new BufferedReader(reader);
        try {
            if (br.ready()) {
                // skip header line
                String s = br.readLine();
                getSplit(s);
            }

            String line = "";
            while (br.ready() && line != null) {
                line = br.readLine();
                if (line != null && line.replace("\u0000", "").length() > 0) {
                    createLine(line, info);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return info;
    }

    private void createLine(String line, BillingInfo info) throws IOException {
        List<BillingPosition> positions = info.getPositions();
        String[] split = getSplit(line);
        BillingPosition position = new BillingPosition();
        position.setDatePosted(split[1]);
        String name = split[2];
        if (name.length() != 0) {
            name = split[8] + " " + name;
        } else {
            name = split[8] + " " + split[3];
        }
        position.setName(name);
        String description = split[4];
        if (description.length() == 0) {
            description = split[8];
        }
        position.setDescription(description);
        position.setTransactionAmount(split[5]);
        position.setTransactionType(getTransactionType(split[8]));
        setId(position);
        positions.add(position);
    }

    private String[] getSplit(String line) {
        return line.split("\t");
    }


    String getTransactionType(String mbankType) {
        if (mbankType.startsWith("SPLATA KREDYTU")) {
            return "DEBIT";
        } else if (mbankType.startsWith("PROWIZJE AUT.")) {
            return "DEBIT";
        } else if (mbankType.startsWith("OPLATA")) {
            return "DEBIT";
        } else if (mbankType.startsWith("\"KREDYT")) {
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
