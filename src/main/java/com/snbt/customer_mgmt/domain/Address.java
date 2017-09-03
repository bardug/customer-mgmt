package com.snbt.customer_mgmt.domain;

public class Address {
    private String streetName;
    private int houseNum;
    private String city;
    private int zip;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", houseNum=" + houseNum +
                ", city='" + city + '\'' +
                ", zip=" + zip +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (houseNum != address.houseNum) return false;
        if (zip != address.zip) return false;
        if (streetName != null ? !streetName.equals(address.streetName) : address.streetName != null) return false;
        return city != null ? city.equals(address.city) : address.city == null;
    }

    @Override
    public int hashCode() {
        int result = streetName != null ? streetName.hashCode() : 0;
        result = 31 * result + houseNum;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + zip;
        return result;
    }
}
