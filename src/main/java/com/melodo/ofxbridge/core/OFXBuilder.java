package com.melodo.ofxbridge.core;

import net.sf.ofx4j.io.OFXWriter;
import net.sf.ofx4j.io.v1.OFXV1Writer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 8, 2009
 * Time: 11:10:53 AM
 */
public class OFXBuilder {

    public String build(BillingInfo billingInfo) throws IOException {
        StringWriter stringWriter = new StringWriter();
        OFXWriter writer = new OFXV1Writer(stringWriter);
        buildData(billingInfo, writer);
        return stringWriter.toString();
    }

    private void buildData(BillingInfo billingInfo, OFXWriter writer) throws IOException {
        writeHeaders(writer);
        writer.writeStartAggregate("OFX");
        writeH1(writer, billingInfo);
        createData(billingInfo, writer);
        writer.writeEndAggregate("OFX");
        writer.close();
    }

    private void createData(BillingInfo billingInfo, OFXWriter writer) throws IOException {

        writer.writeStartAggregate("BANKMSGSRSV1");
        writer.writeStartAggregate("STMTTRNRS");
        writer.writeElement("TRNUID", "1");
        writer.writeStartAggregate("STATUS");
        writer.writeElement("CODE", "0");
        writer.writeElement("SEVERITY", "INFO");
        writer.writeEndAggregate("STATUS");
        writer.writeStartAggregate("STMTRS");
        writer.writeElement("CURDEF", billingInfo.getCurrency());
        writer.writeStartAggregate("BANKACCTFROM");
        writer.writeElement("BANKID", billingInfo.getBankId());
        writer.writeElement("ACCTID", billingInfo.getAccountId());
        writer.writeElement("ACCTTYPE", billingInfo.getAccountType());
        writer.writeEndAggregate("BANKACCTFROM");
        writer.writeStartAggregate("BANKTRANLIST");
        writer.writeElement("DTSTART", billingInfo.getDateFrom());
        writer.writeElement("DTEND", billingInfo.getDateTo());
        //
        createPositions(billingInfo, writer);
        //
        writer.writeEndAggregate("BANKTRANLIST");
        if (billingInfo.getTotalAmmount() != null) {
            writer.writeStartAggregate("LEDGERBAL");
            writer.writeElement("BALAMT", billingInfo.getTotalAmmount());
            writer.writeElement("DTASOF", billingInfo.getDateTo());
            writer.writeEndAggregate("LEDGERBAL");
        }
        writer.writeEndAggregate("STMTRS");
        writer.writeEndAggregate("STMTTRNRS");
        writer.writeEndAggregate("BANKMSGSRSV1");

    }

    private void createPositions(BillingInfo billingInfo, OFXWriter writer) throws IOException {
        List<BillingPosition> billingPositions = billingInfo.getPositions();
        for (BillingPosition billingPosition : billingPositions) {
            writer.writeStartAggregate("STMTTRN");
            writer.writeElement("TRNTYPE", billingPosition.getTransactionType());
            writer.writeElement("DTPOSTED", billingPosition.getDatePosted());
            writer.writeElement("FITID", billingPosition.getId());
            writer.writeElement("TRNAMT", billingPosition.getTransactionAmount());
            writer.writeElement("NAME", billingPosition.getDescription());
            writer.writeElement("MEMO", billingPosition.getName());
            writer.writeEndAggregate("STMTTRN");
        }
    }

    private void writeH1(OFXWriter writer, BillingInfo billingInfo) throws IOException {
        writer.writeStartAggregate("SIGNONMSGSRSV1");
        writer.writeStartAggregate("SONRS");
        writer.writeStartAggregate("STATUS");
        writer.writeElement("CODE", "0");
        writer.writeElement("SEVERITY", "INFO");
        writer.writeEndAggregate("STATUS");
        writer.writeElement("DTSERVER", billingInfo.getCurrentDate());
        writer.writeElement("LANGUAGE", "POL");
        writer.writeEndAggregate("SONRS");
        writer.writeEndAggregate("SIGNONMSGSRSV1");
    }

    private void writeHeaders(OFXWriter writer) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("OFXHEADER", "100");
        headers.put("DATA", "OFXSGML");
        headers.put("VERSION", "102");
        headers.put("SECURITY", "NONE");
        headers.put("ENCODING", "USASCII");
        headers.put("CHARSET", "1252");
        headers.put("COMPRESSION", "NONE");
        headers.put("OLDFILEUID", "NONE");
        headers.put("NEWFILEUID", "NONE");
        writer.writeHeaders(headers);
    }


}
