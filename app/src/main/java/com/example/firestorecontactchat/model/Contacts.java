package com.example.firestorecontactchat.model;

public class Contacts {
    private String contactImage;
    private String contactName;
    private String contactNumber;

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactImage() {
        return contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
