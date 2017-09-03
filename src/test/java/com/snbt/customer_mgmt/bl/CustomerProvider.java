package com.snbt.customer_mgmt.bl;

import com.snbt.customer_mgmt.domain.Address;
import com.snbt.customer_mgmt.domain.ContactInformation;
import com.snbt.customer_mgmt.domain.CreditCard;
import com.snbt.customer_mgmt.domain.Customer;

import java.time.LocalDate;

public class CustomerProvider {

    private CustomerProvider() {
    }

    public static Customer newCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Art");
        customer.setLastName("Vandelay");
        customer.setAddress(newAddress());
        customer.setDateOfBirth(LocalDate.of(1980, 3, 4));
        customer.setContactInformation(newContactInfo());
        customer.setCreditCard(newCreditCard());
        customer.setDrivingLicenseNumber(567890009);
        return customer;
    }

    public static Customer newCustomerWithCredit(CreditCard creditCard) {
        Customer customer = newCustomer();
        customer.setCreditCard(creditCard);
        return customer;
    }

    public static Customer newCustomerNamed(String firstName) {
        Customer customer = newCustomer();
        customer.setFirstName("Art");
        return customer;
    }

    public static CreditCard newCreditCardExpiresIn(LocalDate expiry) {
        CreditCard creditCard = newCreditCard();
        creditCard.setExpiry(expiry);
        return creditCard;
    }

    public static CreditCard newCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("1234-5678-1111-2222");
        creditCard.setExpiry(LocalDate.now().plusYears(3));
        creditCard.setCvv(888);
        return creditCard;
    }

    public static Address newAddress() {
        Address address = new Address();
        address.setCity("Tel Aviv");
        address.setStreetName("Dafna");
        address.setHouseNum(38);
        address.setHouseNum(44234);
        return address;
    }

    public static ContactInformation newContactInfo() {
        ContactInformation contactInfo = new ContactInformation();
        contactInfo.setPhoneNumber("054-2345678");
        contactInfo.setEmailAddress("artv@vandelay-industries.com");
        return contactInfo;
    }

}
