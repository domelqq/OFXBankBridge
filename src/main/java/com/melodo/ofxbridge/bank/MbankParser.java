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
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 8, 2009
 * Time: 2:03:43 PM
 */
public class MbankParser extends AbstractParser {
    public<P extends AbstractOfxProvider> BillingInfo getBillingInfo(Reader reader, P provider) {
        BillingInfo info = new BillingInfo();

        BufferedReader br = new BufferedReader(reader);
        try {
            int nextline = 0;
            String line = "";
            while (br.ready() && line != null) {
                line = br.readLine();
                switch (nextline) {
                    case 1:
                        createCurrency(line, info);
                        break;
                    case 2:
                        createAccountNumber(line, info);
                        break;
                    case 3:
                        createDates(line, info);
                }
                nextline = 0;
                if (line != null) {
                    if (line.startsWith("#Waluta")) {
                        nextline = 1;
                    } else if (line.startsWith("#Numer rachunku")) {
                        nextline = 2;
                    } else if (line.startsWith("#Za okres")) {
                        nextline = 3;
                    } else if (line.startsWith("#Data operacji")) {
                        createLines(br, info);
                    } else if (line.contains("#Saldo koncowe")){
                        createTotal(line,info);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return info;
    }

    private void createTotal(String line, BillingInfo info) {
        String[] strings = getSplit(line);
        String amount = strings[7].replaceAll("PLN","").replaceAll(" ","").replaceAll(",",".");
        info.setTotalAmmount(amount);

    }

    private void createLines(BufferedReader br, BillingInfo info) throws IOException {
        List<BillingPosition> positions = info.getPositions();
        while (br.ready()) {
            String line = br.readLine();
            if (line != null && line.length() > 0) {
                //
                BillingPosition billingPosition = new BillingPosition();
                String[] strings = getSplit(line);
                billingPosition.setDatePosted(strings[0].replace("-", ""));
                billingPosition.setTransactionType(getTransactionType(strings[2]));
                billingPosition.setDescription(strings[2]);
                String memo = strings[5].trim()+" "+strings[3].trim();
                memo = memo.replaceAll("/","");
                memo = memo.replaceAll("\"","");
                memo = memo.trim();
                if (memo.length() == 0){
                    memo = strings[2];
                }
                billingPosition.setName(memo);
                billingPosition.setTransactionAmount(strings[6].replace(" ", "").replace(",", "."));
                setId(billingPosition);
                positions.add(billingPosition);
            } else {
                break;
            }
        }
    }



    private void createDates(String line, BillingInfo info) {
        String[] strings = getSplit(line);
        info.setDateFrom(strings[0].replace(".", ""));
        info.setDateTo(strings[1].replace(".", ""));
    }

    private void createAccountNumber(String line, BillingInfo info) {
        info.setAccountId(getSplit(line, 0));
    }

    private void createCurrency(String line, BillingInfo info) {
        info.setCurrency(getSplit(line, 0));
    }

    private String getSplit(String line, int number) {
        String[] strings = getSplit(line);
        return strings[number];
    }

    private String[] getSplit(String line) {
        return line.split(";");
    }




    String getTransactionType(String mbankType) {
        if (mbankType.startsWith("KAPITALIZACJA ODSETEK")) {
            return "INT";
        } else if (mbankType.startsWith("WYPLATA W BANKOMACIE")) {
            return "ATM";
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

    /*String getTruncatedMemo(String memo) {
        if (memo.startsWith("\"PRZELEW ZEWNETRZNY PRZYCHODZACY ")) {
            return memo.substring("\"PRZELEW ZEWNETRZNY PRZYCHODZACY ".length()).trim();
        } else if (memo.startsWith("\"ZAKUP PRZY UZYCIU KARTY ")) {
            return memo.substring("\"ZAKUP PRZY UZYCIU KARTY ".length()).trim();
        } else if (memo.startsWith("\"RECZNA SPLATA KARTY KREDYT. ")) {
            return memo.substring("\"RECZNA SPLATA KARTY KREDYT. ".length()).trim();
        } else if (memo.startsWith("\"WYPLATA W BANKOMACIE ")) {
            return memo.substring("\"WYPLATA W BANKOMACIE ".length()).trim();
        } else if (memo.startsWith("\"POLECENIE ZAPLATY - ")) {
            return memo.substring("\"POLECENIE ZAPLATY - ".length());
        } else if (memo.startsWith("\"PRZELEW ZEWNETRZNY WYCHODZACY ")) {
            return memo.substring("\"PRZELEW ZEWNETRZNY WYCHODZACY ".length());
            } else if (memo.startsWith("\"PRZELEW WEWNETRZNY PRZYCHODZACY ")) {
            return memo.substring("\"PRZELEW WEWNETRZNY PRZYCHODZACY ".length());
            } else if (memo.startsWith("\"PRZELEW WEWNETRZNY WYCHODZACY ")) {
            return memo.substring("\"PRZELEW WEWNETRZNY WYCHODZACY ".length());
            } else if (memo.startsWith("\"PROWIZJA Z ZAPRZYJAZNIONEGO ATM ")) {
            return memo.substring("\"PROWIZJA Z ZAPRZYJAZNIONEGO ATM ".length());
            } else if (memo.startsWith("\"OPLATA ZA SPRAWDZENIE SALDA ")) {
            return memo.substring("\"OPLATA ZA SPRAWDZENIE SALDA ".length());
            } else if (memo.startsWith("\"OPLATA PRZELEW ZEW.DOWOL.BIEZACY ")) {
            return memo.substring("\"OPLATA PRZELEW ZEW.DOWOL.BIEZACY ".length());
            } else if (memo.startsWith("\"PRZELEW ZEWNETRZNY WYCHODZACY ")) {
            return memo.substring("\"PRZELEW ZEWNETRZNY WYCHODZACY ".length());
            } else if (memo.startsWith("\"PROWIZJA - WYPLATA Z ATM ZAGRANICA ")) {
            return memo.substring("\"PROWIZJA - WYPLATA Z ATM ZAGRANICA ".length());
            } else if (memo.startsWith("\"PRZELEW MTRANSFER WYCHODZACY ")) {
            return memo.substring("\"PRZELEW MTRANSFER WYCHODZACY ".length());
            } else if (memo.startsWith("\"PRZELEW ZEWNETRZNY DO ")) {
            return memo.substring("\"PRZELEW ZEWNETRZNY DO ".length());
            } else if (memo.startsWith("\"PROW. WYPL. GOT. W ODDZ. W KRAJU ")) {
            return memo.substring("\"PROW. WYPL. GOT. W ODDZ. W KRAJU ".length());
            } else if (memo.startsWith("\"WYPLATA KARTA W ODDZIALE ")) {
            return memo.substring("\"WYPLATA KARTA W ODDZIALE ".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());
            } else if (memo.startsWith("\"")) {
            return memo.substring("\"".length());


        } else {
            return memo;
        }
    }*/


}
