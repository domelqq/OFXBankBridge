package com.melodo.ofxbridge.core;

import java.util.List;
import java.util.ArrayList;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 8, 2009
 * Time: 1:45:41 PM
 */
public class BillingInfo {
    private String currency;
    private String bankId;
    private String accountId;
    private String accountType;
    private String dateFrom;
    private String dateTo;
    private List<BillingPosition> positions = new ArrayList<BillingPosition>();
    private String totalAmmount;

    public String getCurrentDate() {
        return "20090908142404";
    }


    public String getCurrency() {
        return currency;
    }

    public String getBankId() {
        return bankId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public List<BillingPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<BillingPosition> positions) {
        this.positions = positions;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDateFrom() {
        return "00000000";
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return "00000000";
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTotalAmmount() {
        return totalAmmount;
    }

    public void setTotalAmmount(String totalAmmount) {
        this.totalAmmount = totalAmmount;
    }

    @Override
    public String toString() {
        return "BillingInfo{" +
                "currency='" + currency + '\'' +
                ", bankId='" + bankId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", positions=" + positions + "\n" +
                ", totalAmmount='" + totalAmmount + '\'' +
                '}';
    }
}
