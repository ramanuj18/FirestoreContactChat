package com.example.firestorecontactchat.model;

public class UserItem {
    private User user;
    private String userMobile;

    public UserItem(User user, String userMobile) {
        this.user = user;
        this.userMobile = userMobile;
    }

    public User getUser() {
        return user;
    }

    public String getUserMobile() {
        return userMobile;
    }
}
