package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer paymentid;
    private float transactionamount;
    private String transactiondate;
    private String transactionplatform;
    private String transactionaccount;
    private String transactionid;
    private Integer orderid;
    private Integer subscriptionid;

    public Integer getSubscriptionid() {
        return subscriptionid;
    }

    public void setSubscriptionid(Integer subscriptionid) {
        this.subscriptionid = subscriptionid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Integer paymentid) {
        this.paymentid = paymentid;
    }

    public float getTransactionamount() {
        return transactionamount;
    }

    public void setTransactionamount(float transactionamount) {
        this.transactionamount = transactionamount;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getTransactionplatform() {
        return transactionplatform;
    }

    public void setTransactionplatform(String transactionplatform) {
        this.transactionplatform = transactionplatform;
    }

    public String getTransactionaccount() {
        return transactionaccount;
    }

    public void setTransactionaccount(String transactionaccount) {
        this.transactionaccount = transactionaccount;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public static String generateHmacSHA256(String data, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {

        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKeySpec);

        byte[] hashedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array to a hexadecimal string
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte hashedByte : hashedBytes) {
            hexStringBuilder.append(String.format("%02x", hashedByte));
        }

        return hexStringBuilder.toString();
    }
}
