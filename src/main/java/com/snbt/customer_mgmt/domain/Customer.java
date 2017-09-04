package com.snbt.customer_mgmt.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;
    private Address address;
    private ContactInformation contactInformation;
    private LocalDate dateOfBirth;
    private CreditCard creditCard;
    private long drivingLicenseNumber;

    public Customer() {
        id = UUID.randomUUID();
    }

    private Customer(UUID uid) {
        this.id = uid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public long getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(long drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public Customer updateBy(Customer other) {
        Customer updated = new Customer(id);
        updated.setFirstName(Optional.ofNullable(other.getFirstName()).orElse(firstName));
        updated.setLastName(Optional.ofNullable(other.getLastName()).orElse(lastName));
        updated.setDateOfBirth(Optional.ofNullable(other.getDateOfBirth()).orElse(dateOfBirth));
        updated.setDrivingLicenseNumber(other.getDrivingLicenseNumber() == 0 ? drivingLicenseNumber : other.getDrivingLicenseNumber());
        updated.setAddress(Optional.ofNullable(other.getAddress()).orElse(address));
        updated.setContactInformation(Optional.ofNullable(other.getContactInformation()).orElse(contactInformation));
        updated.setCreditCard(Optional.ofNullable(other.getCreditCard()).orElse(creditCard));
        return updated;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", contactInformation=" + contactInformation +
                ", dateOfBirth=" + dateOfBirth +
                ", creditCard=" + creditCard +
                ", drivingLicenseNumber=" + drivingLicenseNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (drivingLicenseNumber != customer.drivingLicenseNumber) return false;
        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) return false;
        if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null) return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
        if (contactInformation != null ? !contactInformation.equals(customer.contactInformation) : customer.contactInformation != null)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(customer.dateOfBirth) : customer.dateOfBirth != null)
            return false;
        return creditCard != null ? creditCard.equals(customer.creditCard) : customer.creditCard == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (contactInformation != null ? contactInformation.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (creditCard != null ? creditCard.hashCode() : 0);
        result = 31 * result + (int) (drivingLicenseNumber ^ (drivingLicenseNumber >>> 32));
        return result;
    }
}
