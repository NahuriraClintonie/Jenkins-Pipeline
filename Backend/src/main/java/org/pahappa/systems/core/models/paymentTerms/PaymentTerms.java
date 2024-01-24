package org.pahappa.systems.core.models.paymentTerms;

import org.sers.webutils.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="payment_terms")
public class PaymentTerms extends BaseEntity {


    private String bank;


    private String accountName;


    private String accountNumber;


    private String bankCode;


    private String airtelPayCode;


    private String mtnPayCode;


    private String description;

    private String companyName;

    private String email;

    private String address;

    // Getter and Setter methods for each attribute

    @Column(name = "bank")
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "account_name")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(name = "account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "bank_code")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name = "airtel_pay_code")
    public String getAirtelPayCode() {
        return airtelPayCode;
    }

    public void setAirtelPayCode(String airtelPayCode) {
        this.airtelPayCode = airtelPayCode;
    }

    @Column(name = "mtn_pay_code")
    public String getMtnPayCode() {
        return mtnPayCode;
    }

    public void setMtnPayCode(String mtnPayCode) {
        this.mtnPayCode = mtnPayCode;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
