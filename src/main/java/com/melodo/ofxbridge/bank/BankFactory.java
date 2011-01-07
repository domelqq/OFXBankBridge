package com.melodo.ofxbridge.bank;

import java.util.HashMap;
import java.util.Map;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 10, 2009
 * Time: 12:59:51 PM
 */
public class BankFactory {
    private static Map<String, AbstractConnector> connectorMap = new HashMap<String, AbstractConnector>();
    private static Map<String, AbstractParser> parserMap = new HashMap<String, AbstractParser>();

    public static final String MBANK = "MBANK";

    public static final String PEKAO_24 = "PEKAO24";

    public static final String MBANK__VISA = "MBANK_VISA";

    public static final String MBANK__VISA__HIST = "MBANK_VISA_HIST";

    static {
        connectorMap.put(MBANK, new MbankConnector());
        parserMap.put(MBANK, new MbankParser());

        connectorMap.put(PEKAO_24, new Pekao24Connector());
        parserMap.put(PEKAO_24, new Pekao24Parser());

        connectorMap.put(MBANK__VISA, new MBankVisaCardConnector());
        parserMap.put(MBANK__VISA, new MbankVisaCardParser());

        connectorMap.put(MBANK__VISA__HIST, new MBankVisaCardHistoryConnector());
        parserMap.put(MBANK__VISA__HIST, new MbankVisaCardParser());
    }


    public static AbstractConnector getConnector(String name) {
        return connectorMap.get(name);
    }

    public static AbstractParser getParser(String name) {
        return parserMap.get(name);
    }
}
