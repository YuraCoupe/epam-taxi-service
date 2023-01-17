package com.epam.rd.java.basic.taxiservice.model;

public class User {
    private Integer id;
    private String phoneNumber;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private Double sumSpent;
    private Integer discountRate;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Double getSumSpent() {
        return sumSpent;
    }

    public void setSumSpent(double sumSpent) {
        this.sumSpent = sumSpent;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
}
