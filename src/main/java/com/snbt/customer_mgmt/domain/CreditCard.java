package com.snbt.customer_mgmt.domain;

import java.time.LocalDate;
import java.util.Date;

public class CreditCard {
    private String number;
    private LocalDate expiry;
    private int cvv;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }


    @Override
    public String toString() {
        return "CreditCard{" +
                "number='" + number + '\'' +
                ", expiry=" + expiry +
                ", cvv=" + cvv +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditCard that = (CreditCard) o;

        if (cvv != that.cvv) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        return expiry != null ? expiry.equals(that.expiry) : that.expiry == null;
    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (expiry != null ? expiry.hashCode() : 0);
        result = 31 * result + cvv;
        return result;
    }
}
