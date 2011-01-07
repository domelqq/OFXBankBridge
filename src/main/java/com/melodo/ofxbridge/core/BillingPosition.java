package com.melodo.ofxbridge.core;

/**
 * Sap2Word
 * User: marcindomagaa
 * Date: Sep 8, 2009
 * Time: 1:51:00 PM
 */
public class BillingPosition {
    private String transactionType;
    private String datePosted;
    private String id;
    private String transactionAmount;
    private String name;
    private String description;

    public String getTransactionType() {
        return transactionType;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getId() {
        return id;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BillingPosition{" +
                "transactionType='" + transactionType + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", id='" + id + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}'+ "\n";
    }
}
