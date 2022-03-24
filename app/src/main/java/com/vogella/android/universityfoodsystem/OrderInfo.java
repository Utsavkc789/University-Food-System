package com.vogella.android.universityfoodsystem;

public class OrderInfo {

    String menuItem, itemDescription, restName, firstName, lastName, emailAddress;
    long orderNumber, userID, itemQuantity;
    double itemPrice, orderTax, deliveryFee, orderTotal;

    public OrderInfo(){};

    public OrderInfo(String menuItem, String itemDescription, String restName, String firstName, String lastName, String emailAddress, long orderNumber, long userID, long itemQuantity, double itemPrice, double orderTax, double deliveryFee, double orderTotal) {
        this.menuItem = menuItem;
        this.itemDescription = itemDescription;
        this.restName = restName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.orderNumber = orderNumber;
        this.userID = userID;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.orderTax = orderTax;
        this.deliveryFee = deliveryFee;
        this.orderTotal = orderTotal;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(double orderTax) {
        this.orderTax = orderTax;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }


}
